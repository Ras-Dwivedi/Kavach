

abstract case class KavachDataset[T]() {
  val policy: Policy
  val data: Dataset[(T, Policy)]

  def Map: T => T

  def Reduce: T => T

  def GroupBy: T => T

  def Filter: T => T
}
