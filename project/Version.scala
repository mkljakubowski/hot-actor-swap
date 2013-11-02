import java.util.Date
import sbt.Keys._
import sbt._

/**
 * Helper for generating version file.
 *
 * @author Mikołaj Jakubowski
 */
object Version {

  private val versionProperties =
    """
      |build.version="%s"
      |build.revision="%s:%s"
      |build.timestamp="%s"
      | """.stripMargin

}