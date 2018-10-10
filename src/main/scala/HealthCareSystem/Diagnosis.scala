/**
  * Represents a Diagnosis object associated with a Patient
  * 
  */
case class Diagnosis(diagnosisId: Int, admissionId: Int, patientId: Int, diagnosisICD10Code: Int) {

  def readDiagnosisId(): Int = {
    return diagnosisId;
  }

  def readAdmissionId(): Int = {
    return admissionId
  }

  def readPatientId(): Int = {
    return patientId
  }

  def readDiagnosisICD10Code(): Int = {
    return diagnosisICD10Code
  }

  
}
