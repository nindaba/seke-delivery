function buildAndCompose() {
    ./gradlew bootBuildImage
    docker-compose up -d
}

function createPriceSinkPaymentConnector() {
  curl -X POST 'localhost:8083/connectors' -H "Content-Type: application/json" -d "@price-sink-payment.price.json"
}

function createVolumes() {
    docker volume create services_data
    docker volume create services
}

if [ $1 == '?' ]; then
  echo '--build'
  echo '--volumes'
  echo '--compose'
  echo '--build-compose'
  echo '--price-sink-payment or -psp'
fi

if [ -z $1 ] | [ $1 == '--volumes' ]; then
  createVolumes
fi

if [ -z $1 ] | [ $1 == '--build-compose' ]; then
  buildAndCompose
fi

if [ -z $1 ] | [ $1 == '--compose' ]; then
  docker-compose up -d
fi

if [ -z $1 ] | [ $1 == '--build' ]; then
  buildAndCompose
fi

if [ $1 == '--price-sink-payment' ] | [ $1 == '-psp' ]; then
  createPriceSinkPaymentConnector
fi
