import aws.sdk.kotlin.services.dynamodb.DynamoDbClient
import aws.smithy.kotlin.runtime.client.SdkLogMode

// https://github.com/awslabs/aws-sdk-kotlin/blob/main/docs/debugging.md

suspend fun main() {
    val ddb = DynamoDbClient.fromEnvironment {
        sdkLogMode = SdkLogMode.LogRequestWithBody + SdkLogMode.LogResponseWithBody
    }
    ddb.listTables { }
}
