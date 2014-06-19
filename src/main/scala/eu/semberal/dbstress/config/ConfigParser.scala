package eu.semberal.dbstress.config

import eu.semberal.dbstress.model.{TestUnitConfig, TestUnit, DbConfig, Scenario}
import org.duh.resource._
import org.yaml.snakeyaml.Yaml

import scala.collection.JavaConversions._

object ConfigParser {

  def parseConfigurationYaml(): Scenario = {
    val yaml = new Yaml
    val units = for (x <- this.getClass.getClassLoader.getResourceAsStream("config.yaml").auto) yield {
      yaml.loadAll(x).map(x => Map(x.asInstanceOf[java.util.Map[String, Object]].toList: _*))
    }.map(map => {
      val dbConfig = DbConfig(map("uri").asInstanceOf[String], map("username").asInstanceOf[String],
        map("password").asInstanceOf[String], map("query").asInstanceOf[String])
      val unitConfig = TestUnitConfig(dbConfig, map("repeats").asInstanceOf[Int])
      TestUnit(map("unit_name").asInstanceOf[String], unitConfig, map("parallel_connections").asInstanceOf[Int])
    }).toList
    Scenario(units)
  }
}
