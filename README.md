# data-playground
> 数据游乐场：存储，传输，检索，分析......
## module-1: easy-es-data
> 使用easy-es操作elasticsearch
### 1.connConfig.yml
> es连接信息在此配置
```yaml
easy-es:
  enable: true #默认为true,若为false则认为不启用本框架
  schema: https
  address : x.x.x.x:9200 # es的连接地址,必须含端口 若为集群,则可以用逗号隔开 例如:127.0.0.1:9200,127.0.0.2:9200
  username: x #若无 则可省略此行配置
  password: y #若无 则可省略此行配置

```