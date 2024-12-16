/*
 * Copyright 2023 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package v1.residentialPropertyDisposals.createAmendCgtPpdOverrides

import api.controllers._
import api.models.audit.{AuditEvent, AuditResponse, GenericAuditDetail}
import api.models.auth.UserDetails
import api.models.errors._
import api.services.{AuditService, EnrolmentsAuthService, MtdIdLookupService, NrsProxyService}
import config.AppConfig
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Action, ControllerComponents}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.audit.http.connector.AuditResult
import utils.IdGenerator
import v1.residentialPropertyDisposals.createAmendCgtPpdOverrides.model.response.CreateAmendCgtPpdOverridesAuditData

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CreateAmendCgtPpdOverridesController @Inject() (val authService: EnrolmentsAuthService,
                                                      val lookupService: MtdIdLookupService,
                                                      validatorFactory: CreateAmendCgtPpdOverridesValidatorFactory,
                                                      service: CreateAmendCgtPpdOverridesService,
                                                      auditService: AuditService,
                                                      nrsProxyService: NrsProxyService,
                                                      cc: ControllerComponents,
                                                      val idGenerator: IdGenerator)(implicit ec: ExecutionContext, appConfig: AppConfig)
    extends AuthorisedController(cc) {
  val endpointName = "create-amend-cgt-ppd-overrides"

  implicit val endpointLogContext: EndpointLogContext =
    EndpointLogContext(
      controllerName = "CreateAmendCgtPpdOverridesController",
      endpointName = "createAmendCgtPpdOverrides"
    )

  def createAmendCgtPpdOverrides(nino: String, taxYear: String): Action[JsValue] =
    authorisedAction(nino).async(parse.json) { implicit request =>
      implicit val ctx: RequestContext = RequestContext.from(idGenerator, endpointLogContext)

      val validator = validatorFactory.validator(nino, taxYear, request.body)

      val requestHandler = RequestHandler
        .withValidator(validator)
        .withService { req =>
          nrsProxyService.submitAsync(nino, "itsa-cgt-disposal-ppd", request.body)
          service.createAmend(req)
        }
        .withAuditing(auditHandler(nino, taxYear, request))
        .withNoContentResult(OK)

      requestHandler.handleRequest()
    }

  private def auditHandler(nino: String, taxYear: String, request: UserRequest[JsValue]): AuditHandler = {
    new AuditHandler() {
      override def performAudit(userDetails: UserDetails, httpStatus: Int, response: Either[ErrorWrapper, Option[JsValue]])(implicit
          ctx: RequestContext,
          ec: ExecutionContext): Unit = {

        response match {
          case Left(err: ErrorWrapper) =>
            auditSubmission(
              GenericAuditDetail(
                request.userDetails,
                "1.0",
                Map("nino" -> nino, "taxYear" -> taxYear),
                Some(request.body),
                ctx.correlationId,
                AuditResponse(httpStatus = httpStatus, response = Left(err.auditErrors))
              ))

          case Right(_: Option[JsValue]) =>
            auditSubmission(
              GenericAuditDetail(
                request.userDetails,
                "1.0",
                Map("nino" -> nino, "taxYear" -> taxYear),
                Some(request.body),
                ctx.correlationId,
                AuditResponse(httpStatus = httpStatus, response = Right(Some(Json.toJson(CreateAmendCgtPpdOverridesAuditData(nino, taxYear)))))
              ))
        }
      }
    }
  }

  private def auditSubmission(details: GenericAuditDetail)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[AuditResult] = {
    val event = AuditEvent("CreateAmendCgtPpdOverrides", "Create-Amend-Cgt-Ppd-Overrides", details)
    auditService.auditEvent(event)
  }

}