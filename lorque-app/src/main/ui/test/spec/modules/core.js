'use strict';

describe('Controller: CoreCtrl', function () {

  // load the controller's module
  beforeEach(module('lorque'));

  var CoreCtrl,
      scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope    = $rootScope.$new();
    CoreCtrl = $controller('CoreCtrl', {
      $scope: scope
    });
  }));

  it('shouldn\'t have anything defined in scope', function() {
    expect(scope.something).toBeUndefined();
  });
});
