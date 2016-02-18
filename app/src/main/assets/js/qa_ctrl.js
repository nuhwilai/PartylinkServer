var app = angular.module('PartyApp',[]);

app.controller('BodyController', ['$scope', function($scope) {


	var a1 = new Person(91,"John",'teamA');
	var a2 = new Person(3,"Jane",'teamA');
	var b1 = new Person(92,"Mike",'teamB');
	var b2 = new Person(94,"Yuri",'teamB');
	$scope.teamA= [];
	$scope.teamB= [];
	$scope.score={"A":"00", "B":"00"};
	$scope.round = 1;

	$scope.show = [];
	$scope.status = "";
	$scope.animate = "animated bounceIn";

	Android.onUiReady();
}]);

function Person(id, name, team, icon) {
	this.id = id;
	this.name = name || "Anonymous";
	this.icon = icon || "img/person.png";
	this.team = team;
}

function getPerson(personId, team){
	var aa;
	angular.element(document.body).scope().$apply(
		function($scope){
			if(team != null){
				if(team == 'teamA')
					aa = $scope.teamA.filter(function(p){return p.id==personId;})[0];
				else
					aa = $scope.teamB.filter(function(p){return p.id==personId;})[0];
			}
			else{
				aa = $scope.teamA.filter(function(p){return p.id==personId;})[0];
				if(aa == null)
					aa = $scope.teamB.filter(function(p){return p.id==personId;})[0];
			}
		});
	if(aa == null)
		console.log("ERROR : getPerson id:"+personId+" team"+team);
	return aa;
}