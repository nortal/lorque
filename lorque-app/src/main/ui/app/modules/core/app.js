'use strict';

angular
  .module('lorque', [
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'lorque.query',
    'lorque.settings',
    'lorque.config',
    'lorque.layout'
  ])
  .config(function ($routeProvider) {
    $routeProvider
      .otherwise({
        redirectTo: '/queries'
      });
  });
