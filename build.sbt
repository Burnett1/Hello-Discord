lazy val root = (project in file("."))
    .settings(
      name         := "hello-discord",
      organization := "com.burnett.hellodiscord",
      scalaVersion := "2.11.8",
      libraryDependencies := Seq(
        "net.dv8tion" % "JDA" % "3.1.1_224"
      )
    )

