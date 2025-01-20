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

package v2.residentialPropertyDisposals.createAmendCgtPpdOverrides

import com.google.inject.Singleton
import shared.config.SharedAppConfig
import shared.connectors.DownstreamUri.{IfsUri, TaxYearSpecificIfsUri}
import shared.connectors.{BaseDownstreamConnector, DownstreamOutcome}
import uk.gov.hmrc.http.{HeaderCarrier, HttpClient}
import v2.residentialPropertyDisposals.createAmendCgtPpdOverrides.model.request.CreateAmendCgtPpdOverridesRequestData

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CreateAmendCgtPpdOverridesConnector @Inject() (val http: HttpClient, val appConfig: SharedAppConfig) extends BaseDownstreamConnector {

  def createAmend(request: CreateAmendCgtPpdOverridesRequestData)(implicit
                                                                       hc: HeaderCarrier,
                                                                       ec: ExecutionContext,
                                                                       correlationId: String): Future[DownstreamOutcome[Unit]] = {

    import request._
    import shared.connectors.httpparsers.StandardDownstreamHttpParser._

    val downstreamUri =
      if (taxYear.useTaxYearSpecificApi) {
        TaxYearSpecificIfsUri[Unit](s"income-tax/income/disposals/residential-property/ppd/${taxYear.asTysDownstream}/$nino")
      } else {
        IfsUri[Unit](s"income-tax/income/disposals/residential-property/ppd/$nino/${taxYear.asMtd}")
      }

    put(body, downstreamUri)
  }

}
