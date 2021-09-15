# Match Time Converter

Converter that takes a String representing match time in one format, and convert it to a String representing match
 time in another format.
 
The input match time format:

 `[period] minutes:seconds.milliseconds`

The output format:

` normalTimeMinutes:normalTimeSeconds - period`

When a given period goes into additional time (i.e. > 45:00.000 for first half, > 90.00.000 for the second
half), the added minutes and seconds are represented separately in the format:

`normalTimeMinutes:normalTimeSeconds +additionalMinutes:additionalSeconds - period`

Any input which does not meet the required input format should result in an output of **INVALID**

### Examples
| Input           | Expected Output            |
|-----------------|----------------------------|
| [PM] 0:00.000   | 00:00 – PRE_MATCH          |
| [H1] 0:15.025   | 00:15 – FIRST_HALF         |
| [H1] 3:07.513   | 03:08 – FIRST_HALF         |
| [H1] 45:00.001  | 45:00 +00:00 – FIRST_HALF  |
| [H1] 46:15.752  | 45:00 +01:16 – FIRST_HALF  |
| [HT] 45:00.000  | 45:00 – HALF_TIME          |
| [H2] 45:00.500  | 45:01 – SECOND_HALF        |
| [H3] 90:00.000  | 90:00 +00:01 – SECOND_HALF |
| [FT] 90:00.000  | 90:00 +00:00 – FULL_TIME   |
| 90:00           | INVALID                    |
| [H3] 90:00.000  | INVALID                    |
| [PM] -10:00.000 | INVALID                    |
| FOO             | INVALID                    |

### Extra requirements:

* Mean times (Pre match, half time and full time) should always be the same value
    * Pre match should always be 0:00.000
    * Half time should always be 45:00.000
    * Full time should always be 90:00.000
* There should be a minimum time for each half
    * First half should always be bigger than 0:00.000
    * Second half should always be bigger than 45:00.000
    
## Running the application
The converter can be mainly started in 3 ways:
* Docker
* Jar
* Gradle

### Docker

The image can be downloaded from docker hub:
`docker pull ricardoocorreia/match-time-converter:0.0.1`

After that we can start a container:
`docker run -it --name match-time-converter -p 8080:8080 ricardoocorreia/match-time-converter:0.0.1`

If we need to rebuild the docker image, we can do it with the following command:
`docker build --build-arg JAR_FILE=build/libs/match-time-string-converter-0.0.1.jar -t match-time-converter .`

### Jar

For this execution, we first need to build the project by:
`./gradlew build`

Then we can run the generated jar file:
`java -jar build/libs/match-time-string-converter-0.0.1.jar`

### Gradle

We can also run directly through gradle:
`./graldew bootRun`

**Note**: We can't interact with the application via command-line with this method

## Interacting with the application
The application has 2 entrypoints:
* HTTP
* Command line

### HTTP

The conversion endpoint is available under:
`GET /api/match-time/conversion`

**Note**: Since this endpoint is GET, we need to pass the input string through request parameter, thus we need to
 encode the string.
 
#### Usage examples
`curl -G 'localhost:8080/api/match-time/conversion' --data-urlencode "inputString=[PM] 0:00.000"`
`curl -G 'localhost:8080/api/match-time/conversion' --data-urlencode "inputString=[H1] 0:15.025"`

### Command line
We can also interact with the application through command line by just typing in the terminal where the application
 is running.
 
**Note**: Only works with Docker container and JAR file.
