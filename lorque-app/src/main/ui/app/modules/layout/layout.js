angular.module('lorque.layout', ['templates-main'])
  .controller('HeaderCtrl', function ($scope, $rootScope, $location, $window, config) {
    $scope.url = $window.location.href;
    $scope.title = config.title;
    $scope.api = config.api;

  })
  .directive('angaActiveLink', function ($location) {
    return {
      restrict: 'A',
      link: function (scope, element, attrs) {
        var linkPath = attrs.href;
        scope.$on('$routeChangeSuccess', function () {
          if ($location.path().substring(1) === linkPath.substring(1)) {
            element.parent().addClass('active');
          } else {
            element.parent().removeClass('active');
          }
        });
      }

    };

  });
