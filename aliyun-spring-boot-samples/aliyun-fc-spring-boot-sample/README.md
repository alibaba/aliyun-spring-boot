## Alibaba Cloud Function Compute Sample Application

### Precondition

Make sure that you have the [Serverless-Devs](https://github.com/Serverless-Devs/Serverless-Devs/blob/master/readme_en.md) installed.

You must [configure Serverless-Devs](https://github.com/Serverless-Devs/Serverless-Devs/blob/master/docs/en/command/config.md) before deploy the functions.

### Test locally

Make sure Docker is installed and started in your host.

Run the function:

```bash
s local start
```

Invoke the HTTP function:

<img src="https://img.alicdn.com/imgextra/i1/O1CN01RRqV8b26JAkTRI3zS_!!6000000007640-2-tps-2296-1352.png" width="75%" height="75%">

```bash
curl http://localhost:7040 -d 'hello world'
```

### Deploy to Alibaba Cloud

Run the following command from the project root to deploy.

```bash
s deploy -y
```

Invoke the HTTP function:

<img src="https://img.alicdn.com/imgextra/i4/O1CN01H4KXns1RG3oGeG1Vh_!!6000000002083-2-tps-2328-916.png" width="75%" height="75%">

```bash
curl https://fcspring-helloscf-umysuyquaf.REGION_ID.fcapp.run -d "hello world"

# or

curl http://fcspring.helloscf.ACCOUNT-ID.REGION_ID.fc.devsapp.net -d "hello world"
```

### Tips

The configuration items of `s.yaml` refer [Serverless Devs Description file (YAML) specification](https://github.com/Serverless-Devs/Serverless-Devs/blob/master/docs/en/yaml.md).
