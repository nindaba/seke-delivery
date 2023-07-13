# Seke delivery [Architecture]

## Seke Gateway

<img src="./design/seke-gateway.svg">

## Delivery Service

<img src="./design/delivery-service.svg">

### `/deliveries`

This service will create deliveries

* When a delivery is created, it is stores the package into kafka broker for other services to calculate the price and
  the
  route and other stuff
* It is then stored in redis for reference with all attribute
* updates the delivery once the price has been changed
* starts the delivery once it has been paid
* saves to cassandra
* consumes tracking topic to look for delivered packages to update the records

when delivery creation is not created in 10 min, the package shall be deleted from the service

### `/deliveries/create`

A delivery will not be created in one request but multiple requests, as user start the process of creating the delivery,
each key point will be patched to this endpoint and keep writing to the topics the changes

* creates delivery and assing package uid, customerUid
* assign from address
* assign to address
* create route basic route
* option warehouse or loker as destination
* add weight and all other attributes

as a user I should recieve an deliveryuid of the new created delivery

### `/deliveries/{delivery-id}`

should give me the delivery and patch new changes to it

## Pricing Service

<img src="./design/pricing-service.svg">

### `/prices`

This service will store all the price settings and preform price calculations for the delivery depending on the
following

| Configuration         | Description                                   |
|-----------------------|-----------------------------------------------|
| weight of the package |                                               |
| volume                |                                               |
| isFragile             |                                               |
| type                  | [EXPRESS =2.5h,FAST =5h,NORMAL=24h,BASIC=48h] |
| distance              |                                               |
| warehouses            |                                               |
| parcel locker         |                                               |

* The price configurations will be stored in cassandra, and cached in redis,
* when a delivery or route are produced in topics, the service will consume and calculate the price
* Then store the price in the cache and aslo send it to the pricing topic,
* Redis should be able to store also the configurations used for the price calculation

### `/prices/{delivery-uid}`

This will get the price for delivery with uid

### `/price-configuration`

This endpoint will get all the price configuration, get configuration for code, update configuration, delete
configuration

## Warehouse Service

<img src="./design/warehouse-service.svg">

## Lockers Service

<img src="./design/lockers-service.svg">

### `/lokers`

This service will hold the records of the deliveris which are in a specific locker , and update the tracking
once it is arrived or left the locker
it will also keep records of all the lokers and there status and create keys evertime it needs to be accessed

### `/lokers/{locker-uid}/boxes/{box-uid}`

This wil keep the information of the boxes in the loker and manage the delivery status

### `/address/lockers`

This will register a loker in location, or get lockers in that specific location
this will also return there statuses, if they are fully occupied or some are free

## Tracking Service

<img src="./design/tracking-service.svg">

## Users Service

<img src="./design/users-service.svg">

## Payment Service

<img src="./design/payment-service.svg">

## Drivers Service

<img src="./design/drivers-service.svg">



