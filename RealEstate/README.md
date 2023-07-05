Project Stories.


Done :
*****
1-realEstate list and card
2-add calls and views
3-navigate to whatsapp via click call
4-error page
5-Chalet list in homepage with filters
6-Chalet-card

To Do:
*****
-search in select menu
-add WaterResources in card.
-page list of real estate and chalet.
-create new Post (chalet , post)
-Sing In & Sign Up (front-end)

 
 
Arabic Websites
***************
-template styles and redirect bundle to arabic

To Discusion:
*************
-contact us

 
 
 post condition


// hayde yaane 3adad el suwar

	if (inputParts.size() > Constants.NB_IMAGE_IN_POST_ALLOWED) {
				return Response.status(Status.BAD_REQUEST).entity(Constants.NB_OF_IMAGE_GREATER_NUMBER_OF_IMAGE_ALLOWED)
						.build();

			}

// hayde 3adad el poset lal user
// mana lal challet





	if (appratmentRent.getSpace() < 50) {
				throw new Exception("SPACE_SHOULD_BE_GREATER_50");

			}
			if (appratmentRent.getPrice() < 60) {
				throw new Exception("PRICE_OF_RENT_SHOULD_BE_GREATER_60");
			}


	if (appratmentSell.getSpace() < 50) {
				throw new Exception("SPACE_SHOULD_BE_GREATER_50");

			}
			if (appratmentSell.getSpace() * 100 < appratmentSell.getPrice()) {
				throw new Exception("PRICE_OF_METER_SHOULD_BE_GREATER_THEN_100_DOLLARS");
			}





if (land.getSpace() < 200) {
				throw new Exception("SPACE_SHOULD_BE_GREATER_200");

			}
			if (land.getSpace() * 4 < land.getPrice()) {
				throw new Exception("PRICE_OF_METER_SHOULD_BE_GREATER_THEN_4_DOLLARS");
			}


if (shopRent.getPrice() < 60) {
				throw new Exception("PRICE_OF_RENT_SHOULD_BE_GREATER_60");
			}
			if (shopRent.getSpace() < 40) {
				throw new Exception("SPACE_SHOULD_BE_GREATER_40");

			}


	if (shopSell.getSpace() * 100 < shopSell.getPrice()) {
				throw new Exception("PRICE_OF_METER_SHOULD_BE_GREATER_THEN_100_DOLLARS");
			}
			if (shopSell.getSpace() < 40) {
				throw new Exception("SPACE_SHOULD_BE_GREATER_40");

			}


	if (officeRent.getPrice() < 60) {
				throw new Exception("PRICE_OF_RENT_SHOULD_BE_GREATER_60");
			}
			if (officeRent.getSpace() < 40) {
				throw new Exception("SPACE_SHOULD_BE_GREATER_40");

			}

if (officeSell.getSpace() * 100 < officeSell.getPrice()) {
				throw new Exception("PRICE_OF_METER_SHOULD_BE_GREATER_THEN_100_DOLLARS");
			}
			if (officeSell.getSpace() < 40) {
				throw new Exception("SPACE_SHOULD_BE_GREATER_40");

			}

realEstate.setPostDate(new Date());

realEstate.setPostStatus(PostStatus.PENDING);


w lal chalet 

chalet.setPostDate(new Date());

chalet.setPostStatus(PostStatus.PENDING);


/*
here in post */


private User checkUserConstraint(User user ) throws Exception {

		

		

		

		if (!postType.equals("CHALET")) {
			Long nbOfPost = restateFacade.findUserCountPostPendingOrActive(user.getId());

			// nb of post allowed ;
			// krml is broker bytla3lo zyde
			int nbOfPostAllowed = 0;
			if (user.isBroker()) {
				nbOfPostAllowed = appSinglton.getBrokerNbOfPost();
			}

			if (user.getUserCategory() == UserCategory.REGULAR) {
				nbOfPostAllowed = nbOfPostAllowed + appSinglton.getFreeNbOfPost();

			} else if (user.getUserCategory() == UserCategory.MEDUIM) {

				nbOfPostAllowed = nbOfPostAllowed + appSinglton.getMeduimAccountNbOfPost();

			} else if (user.getUserCategory() == UserCategory.PREMIUM) {
				nbOfPostAllowed = nbOfPostAllowed + appSinglton.getPremuimAccountNbOfPost();
			}

			if (nbOfPost >= nbOfPostAllowed) {
				throw new Exception("EXCEEDED_POST_LIMIT");

			}

		}

		return user;

	}
	
	
	
	if post verfied n7otolo badge 
	
	@Expose
	private boolean isVerfied;
	


iza user maktab 3akre nhotolo shi
bel classs User  
	private boolean isBroker;


enta w 3am tkhod profile picture aw suwar 3an l post t2kd enon 
		String[] imageExtensions = { ".jpg", ".jpeg", ".png", ".gif" }; // Add more image extensions if needed
 enta bthoton hol b compomnent l jsf 
 