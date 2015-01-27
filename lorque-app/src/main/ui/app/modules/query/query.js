'use strict';

angular
  .module('lorque.query', [])
  .config(function ($routeProvider) {
    $routeProvider
      .when('/queries', {
        templateUrl: 'modules/query/query-list.html',
        controller: 'QueryListCtrl'
      })
      .when('/queries/:queryId', {
        templateUrl: 'modules/query/query.html',
        controller: 'QueryCtrl'
      });

  })
  .controller('QueryListCtrl', function ($scope, $resource, $location, config) {
    $scope.queries = $resource(config.api + '/api/v1/queries').query();
    var ws = new WebSocket('ws://localhost:8080/lorque/ws/queries');

    ws.onopen = function () {
      console.log('Socket has been opened!');
    };

    ws.onmessage = function (message) {
      var incomingQuery = JSON.parse(message.data);
      var queryIdx = getQueryIdx(incomingQuery.id);
      if (queryIdx < 0) {
        $scope.queries.push(incomingQuery);
      } else {
        $scope.queries[queryIdx] = incomingQuery;
      }
      $scope.$apply();
    };

    var getQueryIdx = function (queryId) {
      var existingQuery = _.findWhere($scope.queries, {id: queryId});
      return _.indexOf($scope.queries, existingQuery);
    };

    $scope.formatDate = function (dateStr) {
      if (_.isUndefined(dateStr)) {
        return '';
      }
      return moment(dateStr, 'YYYY-MM-DDTHH:mm:ssZ').format('DD.MM.YYYY HH:mm:ss');
    };

    $scope.getStatusButtonClass = function (query) {
      if (query.status === 'SUBMITTED') {
        return 'default';
      } else if (query.status === 'RUNNING') {
        return 'info';
      } else if (query.status === 'COMPLETED') {
        return 'success';
      } else if (query.status === 'FAILED') {
        return 'danger';
      }
    };

    $scope.navigateToQueryDetails = function (queryId) {
      $location.path('/queries/' + queryId);
    }
  })
  .controller('QueryCtrl', function ($scope, $resource, $routeParams, $location, config) {
    if ($routeParams.queryId === 'new') {
      $scope.query = {};
    } else {
      $scope.query = $resource(config.api + '/api/v1/queries/' + $routeParams.queryId).get();
    }
    $scope.submitQuery = function () {
      $resource(config.api + '/api/v1/queries').save($scope.query, function () {
        $location.path('/queries');
      });
    };
  });
