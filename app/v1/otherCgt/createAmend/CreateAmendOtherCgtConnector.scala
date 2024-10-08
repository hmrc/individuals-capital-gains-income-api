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

package v1.otherCgt.createAmend

import api.connectors.DownstreamUri.{Api1661Uri, TaxYearSpecificIfsUri}
import api.connectors.{BaseDownstreamConnector, DownstreamOutcome}
import config.AppConfig
import uk.gov.hmrc.http.{HeaderCarrier, HttpClient}
import v1.otherCgt.createAmend.model.request.CreateAmendOtherCgtRequestData

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CreateAmendOtherCgtConnector @Inject() (val http: HttpClient, val appConfig: AppConfig) extends BaseDownstreamConnector {

  def createAndAmend(request: CreateAmendOtherCgtRequestData)(implicit
      hc: HeaderCarrier,
      ec: ExecutionContext,
      correlationId: String): Future[DownstreamOutcome[Unit]] = {

    import api.connectors.httpparsers.StandardDownstreamHttpParser._
    import request._

    val downstreamUri =
      if (taxYear.useTaxYearSpecificApi) {
        TaxYearSpecificIfsUri[Unit](s"income-tax/income/disposals/other-gains/${taxYear.asTysDownstream}/${nino.nino}")
      } else {
        Api1661Uri[Unit](s"income-tax/income/disposals/other-gains/${nino.nino}/${taxYear.asMtd}")
      }
    put(downstreamUri, body)
  }

}
