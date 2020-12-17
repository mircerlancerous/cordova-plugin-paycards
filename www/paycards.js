Plugin.PayCards = {
	canScan: function(callback, onFail){
		cordova.exec(callback, onFail, 'PayCardsPlugin', 'canSCan', []);
	},
	
	scan: function(callback, onFail){
		let gotScan = function(strCardData){
			let i, cardObj = {}, field, fields = strCardData.split("\n");
			for(i=0; i<fields.length; i++){
				field = fields[i].split(":");
				cardObj[field[0]] = field[1].trim();
			}
			field = cardObj.card_expiry.split("/");
			cardObj.expiry_month = field[0];
			cardObj.expiry_year = field[1];
			callback(cardObj);
		};
		cordova.exec(gotScan, onFail, 'PayCardsPlugin', 'scan', []);
	}
};