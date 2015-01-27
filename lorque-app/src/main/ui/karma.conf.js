// Karma configuration
// http://karma-runner.github.io/0.10/config/configuration-file.html

// npm install karma --save-dev inside project dir
// grunt test

module.exports = function(config) {
  config.set({
    // base path, that will be used to resolve files and exclude
    basePath: '',

    // testing framework to use (jasmine/mocha/qunit/...)
    frameworks: ['jasmine'],

    // list of files / patterns to load in the browser
    files: [
      'app/bower_components/jquery/dist/jquery.js',
      'app/bower_components/angular/angular.js',
      'app/bower_components/bootstrap-sass-official/vendor/assets/javascripts/bootstrap/affix.js',
      'app/bower_components/bootstrap-sass-official/vendor/assets/javascripts/bootstrap/alert.js',
      'app/bower_components/bootstrap-sass-official/vendor/assets/javascripts/bootstrap/button.js',
      'app/bower_components/bootstrap-sass-official/vendor/assets/javascripts/bootstrap/carousel.js',
      'app/bower_components/bootstrap-sass-official/vendor/assets/javascripts/bootstrap/collapse.js',
      'app/bower_components/bootstrap-sass-official/vendor/assets/javascripts/bootstrap/dropdown.js',
      'app/bower_components/bootstrap-sass-official/vendor/assets/javascripts/bootstrap/tab.js',
      'app/bower_components/bootstrap-sass-official/vendor/assets/javascripts/bootstrap/transition.js',
      'app/bower_components/bootstrap-sass-official/vendor/assets/javascripts/bootstrap/scrollspy.js',
      'app/bower_components/bootstrap-sass-official/vendor/assets/javascripts/bootstrap/modal.js',
      'app/bower_components/bootstrap-sass-official/vendor/assets/javascripts/bootstrap/tooltip.js',
      'app/bower_components/bootstrap-sass-official/vendor/assets/javascripts/bootstrap/popover.js',
      'app/bower_components/angular-cookies/angular-cookies.js',
      'app/bower_components/angular-bootstrap/ui-bootstrap-tpls.js',
      'app/bower_components/momentjs/moment.js',
      'app/bower_components/bootstrap-daterangepicker/daterangepicker.js',
      'app/bower_components/bootstrap/dist/js/bootstrap.js',
      'app/bower_components/ng-bs-daterangepicker/src/ng-bs-daterangepicker.js',
      'app/bower_components/ng-table/ng-table.js',
      'app/bower_components/angular-elastic/elastic.js',
      'app/bower_components/angular-chosen-localytics/chosen.js',
      'app/bower_components/respondJS/dest/respond.src.js',
      'app/bower_components/chosen_v1.1.0/chosen.jquery.min.js',
      'app/bower_components/angular-i18n/angular-locale_et-ee.js',
      'app/bower_components/momentjs/lang/et.js',


      'app/bower_components/angular-mocks/angular-mocks.js',
      'app/bower_components/angular-resource/angular-resource.js',
      'app/bower_components/angular-route/angular-route.js',
      'app/scripts/*.js',
      'app/modules/**/*.js',
      //'test/mock/**/*.js',
      'test/spec/**/*.js'
    ],

    // list of files / patterns to exclude
    exclude: [],

    // web server port
    port: 8080,

    // level of logging
    // possible values: LOG_DISABLE || LOG_ERROR || LOG_WARN || LOG_INFO || LOG_DEBUG
    logLevel: config.LOG_INFO,


    // enable / disable watching file and executing tests whenever any file changes
    autoWatch: true,


    // Start these browsers, currently available:
    // - Chrome
    // - ChromeCanary
    // - Firefox
    // - Opera
    // - Safari (only Mac)
    // - PhantomJS
    // - IE (only Windows)
    browsers: ['PhantomJS'],

    // If browser does not capture in given timeout [ms], kill it
    captureTimeout: 5000,


    // Continuous Integration mode
    // if true, it capture browsers, run tests and exit
    singleRun: false
  });
};
