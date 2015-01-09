name := "stackoverflow-bot"

scalaVersion := "2.11.5"

organization := "com.github.xuwei-k"

licenses += ("MIT License", url("http://opensource.org/licenses/MIT"))

homepage := Some(url("https://github.com/xuwei-k/stackoverflow-bot"))

resolvers += Opts.resolver.sonatypeReleases

val twitter4jVersion = "4.0.2"

libraryDependencies ++= (
  ("org.scala-lang" % "scala-compiler" % scalaVersion.value) ::
  ("org.twitter4j" % "twitter4j-core" % twitter4jVersion) ::
  Nil
)

scalacOptions ++= (
  "-deprecation" ::
  "-Xlint" ::
  "-language:postfixOps" ::
  "-language:existentials" ::
  "-language:higherKinds" ::
  "-language:implicitConversions" ::
  "-Ywarn-unused" ::
  "-Ywarn-unused-import" ::
  Nil
)

assemblySettings

AssemblyKeys.jarName in AssemblyKeys.assembly <<= name.map{ name =>
  val df = new java.text.SimpleDateFormat("yyyy-MM-dd-HH-mm")
  s"""${name}-${df.format(new java.util.Date)}-twitter4j-${twitter4jVersion}.jar"""
}

watchSources += file("config.scala")

sourcesInBase := false

resourceGenerators in Compile += task(
  Seq(baseDirectory.value / "build.sbt")
)
