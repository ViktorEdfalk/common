define([], function() {
	'use strict';

	return [ '$scope', '$filter', '$location', '$routeParams', 'ts-bas.certificateService', '$log',
			function($scope, $filter, $location, $routeParams, certificateService, $log) {
				$scope.cert = {};
				$scope.doneLoading = false;
				$scope.shouldBeOpen = false;

				$scope.open = function() {
					$scope.shouldBeOpen = true;
				};

				$scope.close = function() {
					$scope.closeMsg = 'I was closed at: ' + new Date();
					$scope.shouldBeOpen = false;
				};

				$scope.opts = {
					backdropFade : true,
					dialogFade : true
				};
				

				// expose calculated static link for pdf download
				$scope.downloadAsPdfLink = "/moduleapi/certificate/" + $routeParams.certificateId + "/pdf";

				certificateService.getCertificate($routeParams.certificateId, function(result) {
					$scope.doneLoading = true;
					if (result != null) {
						$scope.cert = result;
						
					} else {
						// show error view
						$location.path("/fel");
					}
				}, function(error) {
					$log.debug(error);
				});
			}];
});
