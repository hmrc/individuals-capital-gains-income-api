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

package v2.residentialPropertyDisposals.retrieveAll.def1.model.response

import play.api.libs.json.{JsValue, Json}
import shared.models.domain.Timestamp
import support.UnitSpec
import v2.residentialPropertyDisposals.retrieveAll.def1.model.MtdSourceEnum

class PpdServiceSpec extends UnitSpec {

  val mtdJson: JsValue = Json.parse(
    """
      |{
      |    "ppdYearToDate": 143.22,
      |    "multiplePropertyDisposals": [
      |      {
      |        "source": "hmrc-held",
      |        "submittedOn": "2020-07-06T09:37:17.000Z",
      |        "ppdSubmissionId": "Da2467289108",
      |        "ppdSubmissionDate": "2020-07-06T09:37:17.000Z",
      |        "numberOfDisposals": 3,
      |        "disposalTaxYear": 2022,
      |        "completionDate": "2022-03-08",
      |        "amountOfNetLoss": 1999.99
      |      }
      |    ],
      |    "singlePropertyDisposals": [
      |      {
      |        "source": "hmrc-held",
      |        "submittedOn": "2020-07-06T09:37:17.000Z",
      |        "ppdSubmissionId": "Da2467289108",
      |        "ppdSubmissionDate": "2020-07-06T09:37:17.000Z",
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
      |  }
      |""".stripMargin
  )

  val desJson: JsValue = Json.parse(
    """
      |{
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
      |        "amountOfLoss": 1999.99
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
      |  }
      |""".stripMargin
  )

  val multiplePropertyDisposals: MultiplePropertyDisposals =
    MultiplePropertyDisposals(
      MtdSourceEnum.`hmrc-held`,
      Some(Timestamp("2020-07-06T09:37:17.000Z")),
      "Da2467289108",
      Some(Timestamp("2020-07-06T09:37:17.000Z")),
      Some(3),
      Some(2022),
      Some("2022-03-08"),
      None,
      Some(1999.99)
    )

  val singlePropertyDisposals: SinglePropertyDisposals =
    SinglePropertyDisposals(
      MtdSourceEnum.`hmrc-held`,
      Some(Timestamp("2020-07-06T09:37:17.000Z")),
      "Da2467289108",
      Some(Timestamp("2020-07-06T09:37:17.000Z")),
      Some("2022-02-04"),
      "2022-03-08",
      1999.99,
      Some("2018-04-06"),
      1999.99,
      Some(1999.99),
      Some(5000.99),
      Some(1999.99),
      Some(1999.99),
      Some(1999.99),
      Some(1999.99),
      Some(1999.99),
      None
    )

  val model: PpdService =
    PpdService(
      Some(143.22),
      Some(Seq(multiplePropertyDisposals)),
      Some(Seq(singlePropertyDisposals))
    )

  "PpdService" when {
    "Reads" should {
      "return a valid object" when {
        "a valid json is supplied" in {
          desJson.as[PpdService] shouldBe model
        }
      }
    }

    "writes" should {
      "produce the expected json" in {
        Json.toJson(model) shouldBe mtdJson
      }
    }
  }

}
