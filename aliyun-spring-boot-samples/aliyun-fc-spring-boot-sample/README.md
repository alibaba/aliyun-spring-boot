## Alibaba Cloud Function Compute Sample Application

### Test locally

Make sure Docker is installed and started in your host.

Run the function:

```bash
fun local start
```

Invoke the HTTP function:

```bash
curl http://localhost:8000/2016-08-15/proxy/helloscf/fcspring -d 'hello world'
```

### Deploy to Alibaba Cloud

Package the application in root directory: 

```bash
mvn package
```

You should see the fat jar in the `target/deploy` directory.

Make sure that you have the [Funcraft](https://www.alibabacloud.com/help/doc-detail/155100.htm) installed.

You must [configure funcraft](https://www.alibabacloud.com/help/doc-detail/146702.htm) before deploy the functions.  

Run the following command from the project root to deploy.

```bash
fun deploy
``` 

Invoke the HTTP function:

```bash
curl https://ACCOUNT-ID.REGION_ID.fc.aliyuncs.com/2016-08-15/proxy/helloscf/fcspring/ -d "hello world"
```

### Tips

The configuration items of `template.yaml` refer [Serverless Application Model](https://github.com/alibaba/funcraft/blob/master/docs/specs/2018-04-03.md). 