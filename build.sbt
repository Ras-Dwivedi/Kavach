
name := "Health Care System"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.3.1"

libraryDependencies += "org.apache.spark" %% "spark-mllib" % "2.0.1"

excludeFilter in unmanagedSources := HiddenFileFilter || "HealthCareSystem*" || "VotingSystem*"
