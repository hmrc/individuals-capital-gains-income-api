/*
 * Copyright 2025 HM Revenue & Customs
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

package v3.residentialPropertyDisposals.retrieveNonPpd.def2.fixture

import play.api.libs.json.*
import shared.models.domain.Timestamp
import v3.residentialPropertyDisposals.retrieveNonPpd.def2.model.response.*
import v3.residentialPropertyDisposals.retrieveNonPpd.def2.model.response.ClaimOrElectionsCodes.{BAD, GHO}

object Def2_RetrieveNonPpdResidentialPropertyCgtControllerFixture {

  val disposals: Disposals = Disposals(
    3,
    Some("CGTDISPOSAL01"),
    "2022-02-04",
    "2022-03-08",
    1999.99,
    "2018-04-06",
    1999.99,
    Some(1999.99),
    Some(5000.99),
    Some(1999.99),
    Some(1999.99),
    Some(1999.99),
    1999.99,
    Some(1999.99),
    Some(Seq(GHO, BAD)),
    None,
    Some(1999.99)
  )

  val customerAddedDisposals: CustomerAddedDisposals =
    CustomerAddedDisposals(
      Timestamp("2020-07-06T09:37:17.000Z"),
      Seq(disposals)
    )

  val responseModel: Def2_RetrieveNonPpdResidentialPropertyCgtResponse =
    Def2_RetrieveNonPpdResidentialPropertyCgtResponse(
      Some(customerAddedDisposals)
    )

  val mtdJson: JsValue = Json
    .parse(
      """
      |{
      |  "customerAddedDisposals": {
      |    "submittedOn": "2020-07-06T09:37:17.000Z",
      |    "disposals": [
      |      {
      |        "numberOfDisposals": 3,
      |        "customerReference": "CGTDISPOSAL01",
      |        "disposalDate": "2022-02-04",
      |        "completionDate": "2022-03-08",
      |        "disposalProceeds": 1999.99,
      |        "acquisitionDate": "2018-04-06",
      |        "acquisitionAmount": 1999.99,
      |        "improvementCosts": 1999.99,
      |        "additionalCosts": 5000.99,
      |        "prfAmount": 1999.99,
      |        "otherReliefAmount": 1999.99,
      |        "gainsWithBadr": 1999.99,
      |        "lossesFromThisYear": 1999.99,
      |        "claimOrElectionsCodes": ["GHO", "BAD"],
      |        "lossesFromPreviousYear": 1999.99,
      |        "amountOfNetLoss": 1999.99
      |     }
      |    ]
      |  }
      |}
      |""".stripMargin
    )
    .as[JsObject]

  val downstreamJson: JsValue = Json
    .parse(
      """
      |{
      |  "ppdService": {
      |    "ppdYearToDate": 143.22,
      |    "multiplePropertyDisposals": [
      |      {
      |        "source": "HMRC HELD",
      |        "submittedOn": "2020-07-06T09:37:17Z",
      |        "ppdSubmissionId": "Da2467289108",
      |        "ppdSubmissionDate": "2020-07-06T09:37:17Z",
      |        "numberOfDisposals": 3,
      |        "disposalTaxYear": "2022",
      |        "completionDate": "2022-03-08",
      |        "amountOfNetGain": 1999.99
      |      }
      |    ],
      |    "singlePropertyDisposals": [
      |      {
      |        "source": "HMRC HELD",
      |        "submittedOn": "2020-07-06T09:37:17Z",
      |        "ppdSubmissionId": "Da2467289108",
      |        "ppdSubmissionDate": "2020-07-06T09:37:17Z",
      |        "disposalDate": "2022-02-04",
      |        "completionDate": "2022-03-08",
      |        "disposalProceeds": 1999.99,
      |        "acquisitionDate": "2018-04-06",
      |        "acquisitionAmount": 1999.99,
      |        "improvementCosts": 1999.99,
      |        "additionalCosts": 5000.99,
      |        "prfAmount": 1999.99,
      |        "otherReliefAmount": 1999.99,
      |        "lossesFromThisYear": 1999.99,
      |        "lossesFromPreviousYear": 1999.99,
      |        "amountOfNetGain": 1999.99
      |      }
      |    ]
      |  },
      |  "customerAddedDisposals": {
      |    "submittedOn": "2020-07-06T09:37:17Z",
      |    "disposals": [
      |      {
      |        "numberOfDisposals": 3,
      |        "customerRef": "CGTDISPOSAL01",
      |        "disposalDate": "2022-02-04",
      |        "completionDate": "2022-03-08",
      |        "disposalProceeds": 1999.99,
      |        "acquisitionDate": "2018-04-06",
      |        "acquisitionAmount": 1999.99,
      |        "improvementCosts": 1999.99,
      |        "additionalCosts": 5000.99,
      |        "prfAmount": 1999.99,
      |        "otherReliefAmount": 1999.99,
      |        "gainsWithBADR": 1999.99,
      |        "lossesFromThisYear": 1999.99,
      |        "claimOrElectionsCodes": ["GHO", "BAD"],
      |        "amountOfNetLoss": 1999.99
      |     }
      |    ]
      |  }
      |}
      |""".stripMargin
    )
    .as[JsObject]

}
