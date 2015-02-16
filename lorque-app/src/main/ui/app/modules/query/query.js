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
    };
  })
  .controller('QueryCtrl', function ($scope, $resource, $routeParams, $location, config) {
    if ($routeParams.queryId === 'new') {
      $scope.query = {};
    } else {
      $scope.query = $resource(config.api + '/api/v1/queries/' + $routeParams.queryId).get();
    }
    $scope.query.parameters = $scope.query.parameters || [];
    $scope.$watch('query.sql', function () {
      $scope.query.parameters = getParameters();
      //$scope.paramCount = _.isUndefined($scope.query.sql) ? 0 : ($scope.query.sql.match(/\?/g) || []).length;
    });
    var getParameters = function () {
      var paramCount = _.isUndefined($scope.query.sql) ? 0 : ($scope.query.sql.match(/\?/g) || []).length;
      var parameters = $scope.query.parameters || [];
      while (parameters.length !== paramCount) {
        if (parameters.length < paramCount) {
          parameters.push({});
        } else {
          parameters.pop();
        }
      }
      return parameters;
    };
    $scope.range = function (n) {
      return new Array(n);
    };

    $scope.sqlTypes = [{"code": -7, "name": "BIT"},
      {"code": -6, "name": "TINYINT"},
      {"code": 5, "name": "SMALLINT"},
      {"code": 4, "name": "INTEGER"},
      {"code": -5, "name": "BIGINT"},
      {"code": 6, "name": "FLOAT"},
      {"code": 7, "name": "REAL"},
      {"code": 8, "name": "DOUBLE"},
      {"code": 2, "name": "NUMERIC"},
      {"code": 3, "name": "DECIMAL"},
      {"code": 1, "name": "CHAR"},
      {"code": 12, "name": "VARCHAR"},
      {"code": -1, "name": "LONGVARCHAR"},
      {"code": 91, "name": "DATE"},
      {"code": 92, "name": "TIME"},
      {"code": 93, "name": "TIMESTAMP"},
      {"code": -2, "name": "BINARY"},
      {"code": -3, "name": "VARBINARY"},
      {"code": -4, "name": "LONGVARBINARY"},
      {"code": 0, "name": "NULL"},
      {"code": 1111, "name": "OTHER"},
      {"code": 2000, "name": "JAVA_OBJECT"},
      {"code": 2001, "name": "DISTINCT"},
      {"code": 2002, "name": "STRUCT"},
      {"code": 2003, "name": "ARRAY"},
      {"code": 2004, "name": "BLOB"},
      {"code": 2005, "name": "CLOB"},
      {"code": 2006, "name": "REF"},
      {"code": 70, "name": "DATALINK"},
      {"code": 16, "name": "BOOLEAN"},
      {"code": -8, "name": "ROWID"},
      {"code": -15, "name": "NCHAR"},
      {"code": -9, "name": "NVARCHAR"},
      {"code": -16, "name": "LONGNVARCHAR"},
      {"code": 2011, "name": "NCLOB"},
      {"code": 2009, "name": "SQLXML"},
      {"code": 2012, "name": "REF_CURSOR"},
      {"code": 2013, "name": "TIME_WITH_TIMEZONE"},
      {"code": 2014, "name": "TIMESTAMP_WITH_TIMEZONE"}];

    $scope.submitQuery = function () {
      $resource(config.api + '/api/v1/queries').save($scope.query, function () {
        $location.path('/queries');
      });
    };
  });
