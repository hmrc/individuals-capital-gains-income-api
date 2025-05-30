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

package v1

import config.CgtAppConfig
import play.api.libs.json.JsValue
import uk.gov.hmrc.http.HttpReads.Implicits._
import uk.gov.hmrc.http.{HeaderCarrier, HttpClient, UpstreamErrorResponse}

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class NrsProxyConnector @Inject()(http: HttpClient, appConfig: CgtAppConfig)(implicit ec: ExecutionContext) {

  def submit(nino: String, notableEvent: String, body: JsValue)(implicit hc: HeaderCarrier): Future[Either[UpstreamErrorResponse, Unit]] =
    http.POST[JsValue, Either[UpstreamErrorResponse, Unit]](s"${appConfig.mtdNrsProxyBaseUrl}/mtd-api-nrs-proxy/$nino/$notableEvent", body)

}
