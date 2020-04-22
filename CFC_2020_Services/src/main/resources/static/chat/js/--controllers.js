'use strict';

/* Controllers */

angular.module('springChat.controllers', ['toaster'])
	.controller('ChatController', ['$scope', '$location', '$interval', 'toaster', 'ChatSocket', function($scope, $location, $interval, toaster, chatSocket) {
		  
		var typing = undefined;
		
		$scope.username     = '';
		$scope.sendTo       = 'everyone';
		$scope.participants = [];
		$scope.messages     = [];
		$scope.newMessage   = ''; 
		$scope.nicknames = [];
		$scope.sendMessage = function() {
			var destination = "/app/chat.message";
			var sentPrivateTo = sessionStorage.getItem("sendTo");
			 if(null != sentPrivateTo && sentPrivateTo!="" && sentPrivateTo!="null" ){
				 $scope.sendTo = sentPrivateTo;
			 }
				 
			if($scope.sendTo != "everyone" && $scope.newMessage !='') {
				destination = "/app/chat.private." + $scope.sendTo;
				$scope.messages.unshift({message: $scope.newMessage, username: 'you', priv: true, to: $scope.sendTo,time:new Date()});

			}
			if($scope.sendTo != "everyone" && $scope.newMessage !='') { // Restricting to send to other users
			chatSocket.send(destination, {}, JSON.stringify({message: $scope.newMessage}));
			}
			$scope.newMessage = '';
		};
		
		$scope.startTyping = function() {
			// Don't send notification if we are still typing or we are typing a private message
	        if (angular.isDefined(typing) || $scope.sendTo != "everyone") return;
	        
	        typing = $interval(function() {
	                $scope.stopTyping();
	            }, 500);
	      //  var lnickname = maintainNickName($scope.username);
	        chatSocket.send("/topic/chat.typing", {}, JSON.stringify({username: $scope.username, typing: true/*,nickname:lnickname*/}));
		};
		
		$scope.stopTyping = function() {
			if (angular.isDefined(typing)) {
		        $interval.cancel(typing);
		        typing = undefined;
		    //    var lnickname = maintainNickName($scope.username);
		        chatSocket.send("/topic/chat.typing", {}, JSON.stringify({username: $scope.username, typing: false/*,nickname:lnickname*/}));
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
		
		$scope.readMsg = function(username){
			for(var index in $scope.participants) {
				var participant = $scope.participants[index];
				  console.log("userName:"+$scope.participants[index].isNewMsg);
				if(participant.username == username) {
					$scope.participants[index].isNewMsg = false;
				}else if($scope.participants[index].isNewMsg == true){
					$scope.participants[index].isNewMsg = true;
				}
		  	}
		}
		
		$scope.privateSending = function(username) {
				$scope.sendTo = (username != $scope.sendTo) ? username : 'everyone';
				for(var index in $scope.participants) {
					var participant = $scope.participants[index];
					  console.log("userName:"+$scope.participants[index].isNewMsg);
					if(participant.username == username) {
						$scope.participants[index].isNewMsg = false;
					}else if($scope.participants[index].isNewMsg == true){
						$scope.participants[index].isNewMsg = true;
					}
			  	} 
		};
			
		var initStompClient = function() {
			chatSocket.init('/ws');
			
			chatSocket.connect(function(frame) {
								  
				$scope.username = frame.headers['user-name'];

				chatSocket.subscribe("/app/chat.participants", function(message) {
					$scope.participants = JSON.parse(message.body);
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
				  
				chatSocket.subscribe("/topic/chat.login", function(message) {
					$scope.participants.unshift({username: JSON.parse(message.body).username, typing : false});
				});	
		        	 
				chatSocket.subscribe("/topic/chat.logout", function(message) {
					var username = JSON.parse(message.body).username;
					for(var index in $scope.participants) {
						if($scope.participants[index].username == username) {
							$scope.participants.splice(index, 1);
						}
					}
		        });
		        	 
				chatSocket.subscribe("/topic/chat.typing", function(message) {
					var parsed = JSON.parse(message.body);
					if(parsed.username == $scope.username) return;
				  					
					for(var index in $scope.participants) {
						var participant = $scope.participants[index];
						  
						if(participant.username == parsed.username) {
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
				chatSocket.subscribe("/topic/chat.message", function(message) {
					$scope.messages.unshift(JSON.parse(message.body));
					
		        });
				  
				chatSocket.subscribe("/user/exchange/amq.direct/chat.message", function(message) {
					var parsed = JSON.parse(message.body);
					parsed.priv = true;
					parsed.time = new Date();
					$scope.messages.unshift(parsed);
					for(var index in $scope.participants) {
						var participant = $scope.participants[index];
						  console.log("tt"+participant.username );
						  console.log("dd"+parsed.username)
						if(participant.username == parsed.username) {
							$scope.participants[index].isNewMsg = true;
						}
				  	} 
		        });
				  
				chatSocket.subscribe("/user/exchange/amq.direct/errors", function(message) {
					toaster.pop('error', "Error", message.body);
		        });
		          
			}, function(error) {
				toaster.pop('error', 'Error', 'Connection error ' + error);
				
		    });
		};
		  
		initStompClient();
	}]);
	