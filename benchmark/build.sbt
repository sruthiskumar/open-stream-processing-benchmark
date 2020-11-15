import sbt.Keys.javaOptions
import sbtassembly.AssemblyPlugin.autoImport.PathList

name := "stream-benchmark"
scalaVersion in ThisBuild := "2.11.8"
cancelable in Global := true
resolvers += Resolver.mavenLocal


val jvmOpts = Seq(
  "-Xmx18g"/*,
  "-Xms10g",
  "-XX:+UseParallelGC"*/
)

val extJvmOpts = Seq(
  "-Xmx18g"/*,
  "-XX:+UseParallelGC"*/
)

lazy val common = Project(id = "common-benchmark",
  base = file("common-benchmark"))
  // .enablePlugins(KlarrioDefaultsPlugin)
  // .enablePlugins(JavaAppPackaging)
  .settings(libraryDependencies ++= Dependencies.commonDependencies,
  resolvers ++= Seq("otto-bintray" at "https://dl.bintray.com/ottogroup/maven"),
  resolvers += Resolver.mavenLocal,
  test in assembly := {},
  assemblyMergeStrategy in assembly := {
    case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
    case _ => MergeStrategy.first
  },
  fork in Test := true)

lazy val flink = Project(id = "flink-benchmark",
  base = file("flink-benchmark"))
  .dependsOn(common % "compile->compile;test->test")
  .settings(libraryDependencies ++= Dependencies.flinkDependencies,
    frameworkSettings("Flink", "2.0"))
  .enablePlugins(JavaAppPackaging)

def frameworkSettings(framework: String, versionDocker: String) = Seq(
  mainClass in assembly := Some(s"${framework.toLowerCase}.benchmark.${framework}TrafficAnalyzer"),
  mainClass in(Compile, run) := Some(s"${framework.toLowerCase}.benchmark.${framework}TrafficAnalyzer"),

  test in assembly := {},
  assemblyMergeStrategy in assembly := {
    case PathList("META-INF", xs@_*) => MergeStrategy.discard
    case x => MergeStrategy.first
  },
  version := versionDocker,
  fork in Test := true,
  envVars in Test := Map("DEPLOYMENT_TYPE" -> "local", "MODE" -> "constant-rate")
)