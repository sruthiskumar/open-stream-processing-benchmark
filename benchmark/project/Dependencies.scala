
import sbt._
import Keys._

object Dependencies {

  object Versions {
    // The versions in alphabetic order
    val commonsCli = "1.3.1"
    val commonsIo = "2.5"
    val commonsPool = "2.4.2"

    val commonsLang = "3.5"

    val dropwizardMetrics = "3.2.2"

    val flink = "1.12-SNAPSHOT"
    val flinkStreamingContrib = "1.4.2"
    val flinkSpector = "0.5"

    val heron = "0.17.5"

    val java = "1.8"
    val junit = "4.12"
    val embeddedJmxTrans = "1.2.1"

    val kafka = "0.10.2.1"
    val kafkaBinary = "0.10"
    val kafkaStreams = "2.3.0"

    val logback = "1.2.2"

    val mockito = "1.9.5"

    val snakeYaml = "1.17"
    val spark = "2.4.1"
    val sparkTestingBase = "2.4.0_0.11.0"
    val storm = "1.1.1"
    val scalaBinary = "2.13"
    val scala = "2.13.0"
    val slf4j = "1.7.7"
    val supercsv = "2.1.0"

    val typeSafe = "1.3.1"
    val typeSafePlay = "2.5.14"
    val twitterChill = "0.9.2"

    val servlet = "3.1.0"
    val ws = "2.0.1"
  }

  val commonDependencies: Seq[ModuleID] = Seq(
    "commons-cli" % "commons-cli" % Versions.commonsCli,
    "commons-io" % "commons-io" % Versions.commonsIo,
    "org.apache.commons" % "commons-lang3" % Versions.commonsLang,
    "com.typesafe" % "config" % Versions.typeSafe,
    "javax.servlet" % "javax.servlet-api" % Versions.servlet,
    "javax.ws.rs" % "javax.ws.rs-api" % Versions.ws,
    "junit" % "junit" % Versions.junit % "test",
    "org.jmxtrans.embedded" % "embedded-jmxtrans" % Versions.embeddedJmxTrans,
    "org.yaml" % "snakeyaml" % Versions.snakeYaml, //markup language
    "org.slf4j" % "slf4j-api" % Versions.slf4j,
    "ch.qos.logback" % "logback-classic" % Versions.logback,
    "org.apache.kafka" %% s"kafka" % Versions.kafka % "compile",
    "com.typesafe.play" %% s"play-json" % Versions.typeSafePlay,
    "net.sf.supercsv" % "super-csv" % Versions.supercsv,
    "org.apache.commons" % "commons-pool2" % Versions.commonsPool,
    "org.flinkspector" %% s"flinkspector-datastream" % Versions.flinkSpector
  ).map(_.exclude("log4j", "log4j")
    .exclude("org.slf4j", "slf4j-log4j12"))

  val flinkDependencies: Seq[ModuleID] = commonDependencies ++ Seq(
    "org.apache.flink" % "flink-core" % Versions.flink,
    "org.apache.flink" %% s"flink-streaming-scala" % Versions.flink,
    "org.apache.flink" %% s"flink-clients" % Versions.flink,
    "org.apache.flink" % "flink-metrics-dropwizard" % Versions.flink,
    "org.apache.flink" % "flink-metrics-core" % Versions.flink,
    "org.apache.flink" %% "flink-metrics-jmx" % Versions.flink,
    "org.apache.flink" %% s"flink-runtime" % Versions.flink,
    "org.apache.flink" %% s"flink-connector-kafka" % Versions.flink,
    "org.apache.flink" % "flink-test-utils_2.11" % Versions.flink % "test",
    "org.apache.flink" % "flink-test-utils-junit" % Versions.flink % "test",
    "org.apache.flink" %% s"flink-streaming-contrib" % Versions.flinkStreamingContrib
  ).map(_.exclude("org.apache.zookeeper", "zookeeper")
    .exclude("javax.jms", "jms")
    .exclude("com.sun.jdmk", "jmxtools")
    .exclude("com.sun.jmx", "jmxri")
    .exclude("log4j", "log4j")
    .exclude("org.slf4j", "slf4j-log4j12"))

  // To prevent error: https://github.com/sbt/sbt/issues/3618
  val workaround = {
    sys.props += "packaging.type" -> "jar"
    ()
  }
  lazy val excludeJpountz = ExclusionRule(organization = "net.jpountz.lz4", name = "lz4")

  val jacksonDependencyOverrides = Set("com.fasterxml.jackson.core" % "jackson-core" % "2.6.7",
    "com.fasterxml.jackson.core" % "jackson-databind" % "2.6.7",
    "com.fasterxml.jackson.module" % "jackson-module-scala_2.11" % "2.6.7",
    "net.jpountz.lz4" % "lz4" % "1.3.0")
}