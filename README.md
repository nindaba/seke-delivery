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

### `/warehouses`

This service will hold the records of the deliveris which are in a specific warehouse , and update the tracking once it
is arrived or left the warehouse

It should also be able to calculate the route by

* checks if the delivery will be picked or handled to a warehouse
* if the distance is longer than the CONFIGURED, to break the route to a warehouse
* the source and destination of the delivery and checks if there is a wharehouses closer to the source or destination
* if the warehouse has packages which is going in the same reagoin apart from the warehouse it self

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

### `/tracking`

This service will keep status of the package, by consuming tracking topic and check the route of the package and send
notifications to the customer

* it will create tracking number as soon as the order is paid and It will keep the package details
* The tracking topic will be with messages form each publisher that will be used for notification and updating the
  processes
* they will be steps for delivering a package, which means this service will try to track those steps

| Tracking Messages                       |
|-----------------------------------------|
| PACKAGE CREATED                         |
| PACKAGE OUT TO                          |
| PACKAGE ARRIVED TO WAREHOUSE at ADDRESS |
| PACKAGE OUT FOR DELIVERY                |
| PACKAGE  ARRIVED TO DESTINATION         |
| PACKAGE PEACKED                         |

## Users Service

<img src="./design/users-service.svg">

### `/customers`

This service will store all the customer related details and preferences.

### `/customer/{customer-uid}/addresses`

### `/customer/{customer-uid}/addresses/{code}/default`

This will be used to add, get default, set default

## Payment Service

<img src="./design/payment-service.svg">

### `/payment/{package-uid}`

This will get the payment response from external payment services and if it was successfull it will update the delivery
to paid topic

and if the delivery is canceled it will revert the payment from the external payment service, this needs futher
investigation on payment services

the transaction will be kept in mongo database

## Drivers Service

<img src="./design/drivers-service.svg">

### `/drivers`

This will register Driver who will deliver the package , and if they are active it will show there locations

* Consume PAID Topic, and all the active drivers in that region will see that package in paid topic and notify the
  drivers in that region
* Once the driver has picked the package, it will send an appropriate message to tracking `[OUT TO ${next-desination}]`
* Once the order has arrived, it will send another notification to tracking topic `[ARRIVED AT ${destination}]`
* The driver will have earnings for each delivery and it will be added to his account

### `/drivers/{driver-uid}`

### `/drivers/{driver-uid}/withdraw`

### `/drivers/{driver-uid}/confirm/delivered/{package-id}`

This will be done by a driver and a notification will be sent in the tracker,

* if it was made to be destined to the locker the locker service will recieve the package
* if it was destined to the warehouse, the whare house will recive the package and keep it for ready for peak up for the
  next destination or package arrived for peak up by the owner
* if the code does not match, then the package will not be confirmed

### `/drivers/{driver-uid}/confirm/delivery/{package-id}`

This will be done by a driver, to confirm that he has started delivering the package
and a message will be sent to the tracker topic




