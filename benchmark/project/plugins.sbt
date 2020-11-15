logLevel := Level.Warn
resolvers := Seq(
	"otto-bintray" at "https://dl.bintray.com/ottogroup/maven",
	"Sbt plugins" at "https://dl.bintray.com/sbt/sbt-plugin-releases",
	"bintray-sbt-plugins" at "https://dl.bintray.com/eed3si9n/sbt-plugins/"
)

addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.9.2")
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.15.0")
addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.7.0")
addSbtPlugin("com.typesafe.sbt" %% "sbt-native-packager" % "1.3.2")