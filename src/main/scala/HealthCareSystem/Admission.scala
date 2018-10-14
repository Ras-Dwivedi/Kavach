/**
  * Represents a Admission object associated with a hospital visit
  * 
  */
case class Admission(admissionId: Int, patientId: Int, admissionDate: String, dischargeDate: String) {

  def readAdmissionId(): Int = {
    return admissionId
  }

  def readPatientId(): Int = {
    return patientId
  }

  def readAdmissionDate(): String = {
    return admissionDate
  }

  def readDischargeDate(): String = {
    return dischargeDate
  }
}
