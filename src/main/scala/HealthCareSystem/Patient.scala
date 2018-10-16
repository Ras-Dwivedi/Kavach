/**
  * Represents a Patient object associated with a real medical patient
  * 
  */
case class Patient(patientId: Int, DOB: String, gender: String, martialStatus: String, smokingStatus: String, city: String, age: Int, ageGroup: String) {

  def readId(): Int = {
    return patientId;
  }

  def readDOB(): String = {
    return DOB
  }

  def readGender(): String = {
    return gender
  }

  def readMartialStatus(): String = {
    return martialStatus
  }

  def readSmokingStatus(): String = {
    return smokingStatus
  }

  def readCity(): String = {
    return city
  }

  def readAge(): Int = {
    return age
  }

  def readAgeGroup(): String = {
    return ageGroup
  }
}
