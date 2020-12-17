# cordova-plugin-paycards

A cordova plugin wrapping the terrific done by the Pay.Cards team.
https://pay.cards/

Currently only supports Android as I am not currently equipped to do iOS development; there is a Pay.Cards iOS SDK.

Example Usage
```
Plugin.PayCards.scan(
	function(cardObject){
  	//got card data
  	cardModel = {
			card_holder: "",
			card_number: "",
			expiry_month: "",
			expiry_year: "",
			card_cvd: ""
		};
  },
	function(){
		//scan canceled
	}
);
```
