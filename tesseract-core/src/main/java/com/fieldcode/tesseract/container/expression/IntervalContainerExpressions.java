package com.fieldcode.tesseract.container.expression;

import com.fieldcode.tesseract.IntervalContainerExpression;
import com.fieldcode.tesseract.Taggable.TagPredicate;

import static com.fieldcode.tesseract.container.expression.LogicalOperator.LogicalOperatorType.AND;
import static com.fieldcode.tesseract.container.expression.LogicalOperator.LogicalOperatorType.NOT;
import static com.fieldcode.tesseract.container.expression.LogicalOperator.LogicalOperatorType.OR;

public class IntervalContainerExpressions {

  private IntervalContainerExpressions() {
  }

  private static String[] combine(String first, String... others) {
    String[] ids = new String[others.length + 1];
    ids[0] = first;
    System.arraycopy(others, 0, ids, 1, others.length);
    return ids;
  }

  /**
   * Creates a new {@link CollectionSelector} that selects all collections based on the given filters.
   *
   * @return new expression
   */
  public static CollectionSelector collections(CollectionFilter... filters) {
    return ImmutableCollectionSelector.builder()
        .addFilters(filters)
        .build();
  }

  /**
   * Creates a new {@link CollectionFilter} that selects collection with the given id.
   *
   * @param id id to select
   * @return new filter
   */
  public static CollectionFilter id(String id) {
    return ImmutableCollectionFilterById.builder()
        .addIds(id)
        .build();
  }

  /**
   * Creates a new {@link CollectionFilter} that selects collections with any of the given ids.
   *
   * @param id to select
   * @param others other ids to select
   * @return new filter
   */
  public static CollectionFilter anyId(String id, String... others) {
    return ImmutableCollectionFilterById.builder()
        .addIds(combine(id, others))
        .build();
  }

  public static CollectionFilter tags(TagPredicate query) {
    return ImmutableCollectionFilterByTag.of(query);
  }

  /**
   * Creates a new {@link LogicalOperator} that combines the given operands with logical AND.
   *
   * @param operands for the AND
   * @return new operator
   */
  public static LogicalOperator and(IntervalContainerExpression... operands) {
    return ImmutableLogicalOperator.builder()
        .type(AND)
        .addOperands(operands)
        .build();
  }

  /**
   * Creates a new {@link LogicalOperator} that combines the given operands with logical OR.
   *
   * @param operands for the OR
   * @return new operator
   */
  public static LogicalOperator or(IntervalContainerExpression... operands) {
    return ImmutableLogicalOperator.builder()
        .type(OR)
        .addOperands(operands)
        .build();
  }

  /**
   * Creates a new {@link LogicalOperator} that negates the given operands.
   *
   * @param operands to negate
   * @return new operator
   */
  public static LogicalOperator not(IntervalContainerExpression... operands) {
    return ImmutableLogicalOperator.builder()
        .type(NOT)
        .addOperands(operands)
        .build();
  }

}
