import aws.sdk.kotlin.services.dynamodb.DynamoDbClient
import aws.sdk.kotlin.services.s3.S3Client
import aws.smithy.kotlin.runtime.content.ByteStream
import aws.smithy.kotlin.runtime.content.fromFile
import aws.smithy.kotlin.runtime.util.length
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import java.io.File

fun main() { runBlocking {
    val s3 = S3Client.fromEnvironment {  }
    val ddb = DynamoDbClient.fromEnvironment {  }

    // Basic async calls via suspend

    val buckets = async { s3.listBuckets { } }
    val tables = async { ddb.listTables { } }

    val bucketCount = buckets.await().buckets?.length
    val tableCount = tables.await().tableNames?.length


    println("Found $bucketCount buckets and $tableCount tables")

    // Dynamic async calls via collection

    val filenames = listOf(
        "bell.mp3",
        "cymbal.mp3",
        "dog-bark.mp3",
    )
    filenames.map { filename ->
        async {
            println("Uploading $filename...")
            s3.putObject {
                bucket = "ktwb-demo"
                key = "upload-test/$filename"
                body = ByteStream.fromFile(File("/tmp/$filename"))
            }
            println("Done uploading $filename")
        }
    }.awaitAll()
} }
