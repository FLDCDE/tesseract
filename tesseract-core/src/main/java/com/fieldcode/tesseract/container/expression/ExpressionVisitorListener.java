package com.fieldcode.tesseract.container.expression;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.fieldcode.tesseract.IntervalCollection;
import com.fieldcode.tesseract.IntervalContainer;
import com.fieldcode.tesseract.IntervalContainerExpression;
import com.fieldcode.tesseract.IntervalContainerExpression.FallbackStrategy;
import com.fieldcode.tesseract.collection.IntervalCollections;
import com.fieldcode.tesseract.container.expression.LogicalOperator.LogicalOperatorType;

import static com.fieldcode.tesseract.IntervalContainerExpression.FallbackStrategy.ALWAYS;
import static com.fieldcode.tesseract.IntervalContainerExpression.FallbackStrategy.EMPTY;
import static com.fieldcode.tesseract.IntervalContainerExpression.FallbackStrategy.ERROR;
import static com.fieldcode.tesseract.container.expression.LogicalOperator.LogicalOperatorType.AND;
import static com.fieldcode.tesseract.container.expression.LogicalOperator.LogicalOperatorType.NOT;
import static com.fieldcode.tesseract.container.expression.LogicalOperator.LogicalOperatorType.OR;
import static com.google.common.base.Preconditions.checkArgument;
import static java.util.stream.Collectors.toList;

public class ExpressionVisitorListener implements NodeVisitor<Stream<IntervalCollection>> {

  private static final Map<LogicalOperatorType, Function<List<IntervalCollection>, IntervalCollection>> REDUCERS = Map.of(
      OR, IntervalCollections::or,
      AND, IntervalCollections::and,
      NOT, IntervalCollections::not
  );

  private static final Map<FallbackStrategy, Supplier<IntervalCollection>> FALLBACK_COLLECTIONS = Map.of(
      EMPTY, IntervalCollections::empty,
      ALWAYS, IntervalCollections::always,
      ERROR, ExpressionVisitorListener::noCollectionFoundError
  );

  private final IntervalContainer container;

  private ExpressionVisitorListener(IntervalContainer container) {
    this.container = container;
  }

  /**
   * Evaluates the given expression and returns the resulting collection.
   *
   * @param container container to evaluate the expression on
   * @param root expression to evaluate
   * @return resulting collection
   */
  public static IntervalCollection evaluate(IntervalContainer container, IntervalContainerExpression root) {
    return new ExpressionVisitorListener(container).evaluate(root);
  }

  private static <T> T noCollectionFoundError() {
    throw new IllegalArgumentException("No collection found for expression");
  }

  private static Supplier<IntervalCollection> fallback(IntervalContainerExpression node) {
    var fallbackStrategy = node.getFallbackStrategy();
    return FALLBACK_COLLECTIONS.get(fallbackStrategy);
  }

  private static Function<List<IntervalCollection>, IntervalCollection> reducer(LogicalOperator node) {
    return REDUCERS.get(node.getType());
  }

  private Stream<IntervalCollection> operands(LogicalOperator node) {
    return node.getOperands()
        .stream()
        .flatMap(this::visit);
  }

  private IntervalCollection evaluate(IntervalContainerExpression root) {
    return reduce(
        visit(root),
        IntervalCollections::and,
        FALLBACK_COLLECTIONS.get(root.getFallbackStrategy())
    );
  }

  private Stream<IntervalCollection> visit(IntervalContainerExpression expression) {
    checkArgument(expression instanceof InternalExpressionNode, "Expression object must be an instance of InternalExpressionNode [%s]", expression.getClass());
    return ((InternalExpressionNode) expression).visit(this);
  }

  @Override
  public Stream<IntervalCollection> visit(CollectionSelector node) {

    var filter = node.getFilters()
        .stream()
        .filter(InternalCollectionFilter.class::isInstance)
        .map(InternalCollectionFilter.class::cast)
        .map(InternalCollectionFilter::getPredicate)
        .reduce(Predicate::and)
        .orElse(__ -> true);

    var collections = container.getAll()
        .stream()
        .filter(filter)
        .map(IntervalCollection.class::cast)
        .collect(toList());

    return collections.isEmpty()
        ? Stream.of(fallback(node).get())
        : collections.stream();

  }

  @Override
  public Stream<IntervalCollection> visit(LogicalOperator node) {
    var operands = operands(node);
    var reducer = reducer(node);
    var fallback = fallback(node);

    return Stream.of(
        reduce(operands, reducer, fallback)
    );
  }

  private IntervalCollection reduce(Stream<IntervalCollection> input, Function<List<IntervalCollection>, IntervalCollection> reducer, Supplier<IntervalCollection> fallback) {
    var collections = input.collect(toList());

    return collections.isEmpty()
        ? fallback.get()
        : reducer.apply(collections);

  }

}
