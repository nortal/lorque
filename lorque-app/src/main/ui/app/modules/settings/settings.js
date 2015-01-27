'use strict';

angular
  .module('lorque.settings', [])
  .config(function ($routeProvider) {
    $routeProvider
      .when('/settings', {
        templateUrl: 'modules/settings/settings.html',
        controller: 'SettingsCtrl'
      });
  })
  .controller('SettingsCtrl', function ($scope, $resource, config) {
    $scope.settings = $resource(config.api + '/api/v1/settings').query();
  });
