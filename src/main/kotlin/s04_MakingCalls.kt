import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.GetObjectRequest
import aws.sdk.kotlin.services.s3.model.ListObjectsV2Request
import aws.smithy.kotlin.runtime.content.decodeToString
import aws.smithy.kotlin.runtime.content.toByteArray
import aws.smithy.kotlin.runtime.content.writeToFile
import java.nio.file.Paths

suspend fun main() {
    val s3 = S3Client.fromEnvironment { }

    // Request DSL syntax

    val req = ListObjectsV2Request {
        bucket = "ktwb-demo"
    }
    val response = s3.listObjectsV2(req)
    println("Found objects:")
    response.contents?.forEach { println(it.key) }

    // Operation DSL syntax

    val response2 = s3.listObjectsV2 {
        bucket = "ktwb-demo"
        prefix = "nested"
    }
    println("Found objects:")
    response2.contents?.forEach { println(it.key) }

    // Streaming operation

    println()
    val getObjReq = GetObjectRequest {
        bucket = "ktwb-demo"
        key = "nested/meow_party.gif"
    }
    s3.getObject(getObjReq) { response ->
        val path = Paths.get("/tmp/the_gif.gif")
        val bytesWritten = response.body?.writeToFile(path)
        println("Wrote $bytesWritten bytes to $path")

        response.body?.toByteArray()
        response.body?.decodeToString()
    }
}
