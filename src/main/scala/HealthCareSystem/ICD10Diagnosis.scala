/**
  * Represents a ICD10Diagnosis object associated with a particular ICD10 code
  * 
  */
case class ICD10Diagnosis(ICD10Code: Int, diagnosisDescription: String) {

  def readICD10Code(): Int = {
    return ICD10Code
  }

  def readDiagnosisDescription(): String = {
    return diagnosisDescription
  }
}
