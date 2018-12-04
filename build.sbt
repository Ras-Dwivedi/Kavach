
name := "Health Care System"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.3.1"

libraryDependencies += "org.apache.spark" %% "spark-mllib" % "2.0.1"

// scalatest.
libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.5"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"

excludeFilter in unmanagedSources := HiddenFileFilter || "HealthCareSystem*" || "VotingSystem*"
