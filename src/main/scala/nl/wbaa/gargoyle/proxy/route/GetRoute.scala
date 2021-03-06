package nl.wbaa.gargoyle.proxy.route

import akka.http.scaladsl.server.Directives._
import com.typesafe.scalalogging.LazyLogging
import nl.wbaa.gargoyle.proxy.providers.StorageProvider
import nl.wbaa.gargoyle.proxy.route.CustomDirectives._
import nl.wbaa.gargoyle.proxy.response.ResponseHelpers._

import scala.util.{Failure, Success, Try}

case class GetRoute()(implicit provider: StorageProvider) extends LazyLogging {

  // accepts all GET requests
  def route() =
    validateToken { tokenOk =>
      get {
        extractRequestContext { ctx =>
          Try(provider.translateRequest(ctx.request)) match {
            case Success(s3Response) =>
              stringComplete(s3Response)
            case Failure(ex) => // all exceptions for now
              throw new Exception(ex.getMessage)
          }
        }
      }
    }

}