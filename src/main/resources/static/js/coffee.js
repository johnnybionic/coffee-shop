var coffeeApp = angular.module('coffeeApp', ['ngResource', 'ui.bootstrap']);

coffeeApp.factory('CoffeeOrder', function ($resource) {
    return $resource('/service/coffeeshop/:id/order/',
        {id: '@coffeeShopId'}, {}
        );
});

// /coffeeshop/nearest/{latitude}/{longitude}
coffeeApp.factory('CoffeeShopLocator', function ($resource) {
    return $resource('/service/coffeeshop/nearest/:latitude/:longitude',
        {latitude: '@latitude', longitude: '@longitude'}, {}
    );
});

coffeeApp.controller('CoffeeShopController', function ($scope, $window, CoffeeShopLocator) {
    $scope.findCoffeeShopNearestToMe = function () {
        window.navigator.geolocation.getCurrentPosition(function (position) {
            console.log("Calling getCoffeeShopAt: " + position.coords.latitude + " " + position.coords.longitude);
            $scope.getCoffeeShopAt(position.coords.latitude, position.coords.longitude)
        }, null);
    };

    $scope.getCoffeeShopAt = function (latitude, longitude) {
        console.log("getCoffeeShopAt: " + latitude + " " + longitude);
        CoffeeShopLocator.get({latitude: latitude, longitude: longitude}).$promise
            .then(
                function (value) {
                    $scope.nearestCoffeeShop = value;
                })
            .catch(
                function (value) {
                    // get default - this should really be done on the web side, to prevent two calls
                    var defaultLatitude = 51.5357461;
                    var defaultLongitude = -0.125133;
                    console.log("Failed - using default " + defaultLatitude + " " + defaultLongitude);

                    $scope.getCoffeeShopAt(defaultLatitude, defaultLongitude);
                });
    };
});

// TODO: get the list from the server
coffeeApp.controller('DrinksController', function ($scope, CoffeeOrder) {
    $scope.types = [
        {name: 'Americano', family: 'Coffee'},
        {name: 'Latte', family: 'Coffee'},
        {name: 'English Breakfast', family: 'Tea'},
        {name: 'Mocha', family: 'Coffee'}
    ];

    $scope.sizes = ['Small', 'Medium', 'Large', 'Heroic', 'Fatal'];
    $scope.messages = [];

    // first funtion is 'on success'
    $scope.giveMeCoffee = function () {
        CoffeeOrder.save({id:1}, $scope.drink,
          function (order) {
              $scope.messages.push({type:"alert alert-success", msg: 'Order sent', orderId: order.id});
              console.log($scope.messages[0]);
          }
         )
    };

    $scope.closeAlert = function (index) {
        $scope.messages.splice(index, 1);
    }
});