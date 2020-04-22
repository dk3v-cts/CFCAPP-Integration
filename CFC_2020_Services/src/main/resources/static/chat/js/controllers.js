'use strict';

/* Controllers */

angular.module('springChat.controllers', ['toaster'])
	.controller('ChatController', ['$scope', '$location', '$interval', 'toaster', 'ChatSocket', function ($scope, $location, $interval, toaster, chatSocket) {

		var typing = undefined;

		$scope.username = '';
		$scope.sendTo = '';
		$scope.participants = [];
		$scope.messages = [];
		$scope.newMessage = '';
		$scope.nicknames = [];
		$scope.sendToIndex = -1;
		$scope.sendMessage = function () {
			var destination = "/app/chat.message";
			var sentPrivateTo = sessionStorage.getItem("sendTo");
			if (null != sentPrivateTo && sentPrivateTo != "" && sentPrivateTo != "null") {
				$scope.sendTo = sentPrivateTo;
			}

			if ($scope.sendTo != '' && $scope.newMessage != '') {
				destination = "/app/chat.private." + $scope.sendTo;
				$scope.messages.push({ message: $scope.newMessage, username: 'you', priv: true, to: $scope.sendTo, time: new Date() });

			}
			if ($scope.sendTo != '' && $scope.newMessage != '') { // Restricting to send to other users
				chatSocket.send(destination, {}, JSON.stringify({ message: $scope.newMessage }));
				setTimeout(function () { $scope.scrollMsgInView(2) }, 200);
			}
			$scope.newMessage = '';
		};
		$scope.returnToHome = function () {
			$scope.sendTo = '';
			$scope.participantSearchText = '';
			$scope.sendToIndex = -1;
		}
		$scope.startTyping = function () {
			// Don't send notification if we are still typing or we are typing a private message
			clearTimeout(typing);
			typing = setTimeout(function () {
				$scope.stopTyping();
			}, 1000);
			//  var lnickname = maintainNickName($scope.username);
			chatSocket.send("/topic/chat.typing", {}, JSON.stringify({ username: $scope.username, typing: true/*,nickname:lnickname*/ }));
		};

		$scope.stopTyping = function () {
			if (angular.isDefined(typing)) {
				clearTimeout(typing);
				typing = undefined;
				//    var lnickname = maintainNickName($scope.username);
				chatSocket.send("/topic/chat.typing", {}, JSON.stringify({ username: $scope.username, typing: false/*,nickname:lnickname*/ }));
			}
		};

		/*	$scope.maintainNickName = function(username){
				for(var index in $scope.nicknames) {
					var participant = $scope.nicknames[index];
					if(participant.username == username) {
						return participant.nickname;
					}
					}
			}*/

		$scope.readMsg = function (username) {
			for (var index in $scope.participants) {
				var participant = $scope.participants[index];
				console.log("userName:" + $scope.participants[index].isNewMsg);
				if (participant.username == username) {
					$scope.participants[index].isNewMsg = false;
				} else if ($scope.participants[index].isNewMsg == true) {
					$scope.participants[index].isNewMsg = true;
				}
			}
		}

		$scope.privateSending = function (username) {
			$scope.sendTo = (username != $scope.sendTo) ? username : '';
			for (var index in $scope.participants) {
				var participant = $scope.participants[index];
				console.log("userName:" + $scope.participants[index].isNewMsg);
				if ($scope.sendTo == participant.username) {
					$scope.sendToIndex = index;
				}
				if (participant.username == username) {
					$scope.participants[index].isNewMsg = false;
				} else if ($scope.participants[index].isNewMsg == true) {
					$scope.participants[index].isNewMsg = true;
				}
			}
		};

		$scope.handleParticipantsLogin = function(msg, status) {
			var name = JSON.parse(msg.body).username;
			var nickName = JSON.parse(msg.body).nickname;
			console.log("handleParticipantsLogin"+JSON.parse(msg.body).nickname)
			if (!!name) {
				var index = $scope.participants.findIndex(function(val) {
					return (val.username == name);
				});
				var chatUser = {
					username: name,
					isOnline: status,
					status: status ? 'Online' : 'Offline',
					typing: false,
					nickname: nickName
				}
				if (index > -1) {
					$scope.participants[index] = chatUser;
				} else {
					$scope.participants.push(chatUser);
				}
			}
		};
		$scope.scrollMsgInView = function (offset) {
			var ele = document.getElementById('textMsg' + ($scope.messages.length - offset));
			if (!!ele) {
				ele.scrollIntoView();
			}
		};

		var initStompClient = function () {
			chatSocket.init('/ws');

			chatSocket.connect(function (frame) {

				$scope.username = frame.headers['user-name'];
				var nicknameCookie = document.cookie;
				var nicknameVar ='';
				if(nicknameCookie.includes('nickname'))
				{
					nicknameVar = nicknameCookie.split('nickname=')[1];
					}				
				chatSocket.subscribe("/app/chat.participants", function (message) {
					var chatUserList = JSON.parse(message.body);
					$scope.participants = chatUserList.map(function(val) {
						val.isOnline = true;
						val.status = 'Online';
						val.typing = false;
						return val;
					})
					/*var nicknameCookie = document.cookie;
					
					for(var index in $scope.participants) {
						console.log("$scope.username"+$scope.username+"nick"+nicknameCookie+"kk"+$scope.participants[index].username+"::kkkkk:: "+nicknameCookie.includes('nickname'));
						if($scope.participants[index].username == $scope.username && nicknameCookie.includes('nickname')) {
							$scope.nicknames.unshift({username: $scope.username, nickname:nicknameCookie.split('nickname=')[1]});
						}
					}
					chatSocket.send("/topic/nicknames.update", {}, JSON.stringify($scope.nicknames));
					console.log("nicknames-->"+$scope.nicknames);*/
				});
				chatSocket.send("/app/chat.addUser",{},
					    JSON.stringify({username: $scope.username, nickname: nicknameVar}));
				
				chatSocket.subscribe("/topic/chat.login", function (message) {
					$scope.handleParticipantsLogin(message, true);
				});

				chatSocket.subscribe("/topic/chat.logout", function (message) {
					$scope.handleParticipantsLogin(message, false);
				});

				chatSocket.subscribe("/topic/chat.typing", function (message) {
					var parsed = JSON.parse(message.body);
					if (parsed.username == $scope.username) return;

					for (var index in $scope.participants) {
						var participant = $scope.participants[index];

						if (participant.username == parsed.username) {
							$scope.participants[index].typing = parsed.typing;
						}
					}
				});

				/*      	 
						  chatSocket.subscribe("/topic/nicknames.update", function(message) {
							  var parsed = [];
							  parsed =JSON.parse(message.body);
						  	
							  console.log(parsed);
							  var parsedUser = function(){
								  for(var user in parsed){
								  if(user.nickname != undefined){
									  return user;
									  console.log(user+"**user")
									  break;
										  }
									  }
								  };
							  if(parsedUser.username == $scope.username) return;
											  	
							  for(var index in $scope.nicknames) {
								  var nickUser = $scope.nicknames[index];
								    
								  if(nickUser.username == parsedUser.username) {
									  $scope.nicknames[index].nickname = parsedUser.nickname;
									  for(var index in $scope.participants) {
										  var participant = $scope.participants[index];
										    
										  if(participant.username == nickUser.username) {
											  $scope.participants[index].nickname = parsedUser.nickname;
										  }
										  } 
								  }
								  } 
						  });
							   */
				chatSocket.subscribe("/topic/chat.message", function (message) {
					$scope.messages.push(JSON.parse(message.body));

				});
				chatSocket.subscribe("/user/exchange/amq.direct/chat.message", function (message) {
					var parsed = JSON.parse(message.body);
					parsed.priv = true;
					parsed.time = new Date();
					$scope.messages.push(parsed);
					for (var index in $scope.participants) {
						var participant = $scope.participants[index];
						console.log("tt" + participant.username);
						console.log("dd" + parsed.username)
						if (participant.username == parsed.username && $scope.sendTo != parsed.username) {
							$scope.participants[index].isNewMsg = true;
						}
					}
					setTimeout(function () { $scope.scrollMsgInView(1) }, 200);
				});

				chatSocket.subscribe("/user/exchange/amq.direct/errors", function (message) {
					toaster.pop('error', "Error", message.body);
				});

			}, function (error) {
				toaster.pop('error', 'Error', 'Connection error ' + error);

			});
		};

		initStompClient();
	}]);
