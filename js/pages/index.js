window.onload = function () {
  let pollButtons = document.querySelectorAll(".poll-options button");
  let voteCount = document.querySelector(".vote-count");

  for (let i = 0; i < pollButtons.length; i++) {
    pollButtons[i].addEventListener("click", function () {
      for (let j = 0; j < pollButtons.length; j++) {
        pollButtons[j].style.backgroundColor = "transparent";
        pollButtons[j].style.color = "#1b1c1c";
        pollButtons[j].querySelector("span").style.opacity = "0";
      }

      this.style.backgroundColor = "#000000";
      this.style.color = "#ffffff";
      this.querySelector("span").style.opacity = "0.7";

      voteCount.innerText = "Thank you for voting";
    });
  }

  let searchInput = document.querySelector(".search input");
  let issueCards = document.querySelectorAll(".issue-card");
  let feedItems = document.querySelectorAll(".feed-item");

  if (searchInput) {
    searchInput.addEventListener("keyup", function () {
      let searchText = searchInput.value.toLowerCase();

      for (let i = 0; i < issueCards.length; i++) {
        let cardText = issueCards[i].innerText.toLowerCase();

        if (cardText.indexOf(searchText) > -1) {
          issueCards[i].style.display = "flex";
        } else {
          issueCards[i].style.display = "none";
        }
      }

      for (let i = 0; i < feedItems.length; i++) {
        let feedText = feedItems[i].innerText.toLowerCase();

        if (feedText.indexOf(searchText) > -1) {
          feedItems[i].style.display = "block";
        } else {
          feedItems[i].style.display = "none";
        }
      }
    });
  }
};
