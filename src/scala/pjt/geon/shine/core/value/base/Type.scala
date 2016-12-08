package pjt.geon.shine.core.value.base

/**
 * === type trait ===
 *
 * this {@code trait} for type
 */
trait Type {
  /**
   * @define insert new value to XXX_Type
   * @param value value for create new type
   * */
  def apply(value : Any) : Unit
}
