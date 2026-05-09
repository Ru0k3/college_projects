"use strict";

import {
  POLL_OPTION_IDS,
  getUserVote,
  loadPollData,
  registerVote,
} from "../utils/poll-data.js";

const DOM = {
  voteButtons: [],
  voteMessage: null,
  totalVotesLabel: null,
};

function getOptionLabelById(pollData, voteId) {
  const selectedOption = pollData.options.find((option) => option.id === voteId);
  return selectedOption ? selectedOption.name : "Unknown option";
}

function setSelectedButton(voteId) {
  DOM.voteButtons.forEach((button) => {
    const isSelected = button.dataset.vote === voteId;
    button.classList.toggle("vote-btn--selected", isSelected);
  });
}

function updateTotalVotesLabel(totalVotes) {
  if (!DOM.totalVotesLabel) {
    return;
  }

  DOM.totalVotesLabel.textContent = `${totalVotes.toLocaleString()} total votes cast`;
}

function showVoteMessage(text) {
  if (!DOM.voteMessage) {
    return;
  }

  DOM.voteMessage.textContent = text;
}

function hydrateInitialState() {
  const pollData = loadPollData();
  const previousVote = getUserVote();

  updateTotalVotesLabel(pollData.totalVotes);

  if (previousVote) {
    setSelectedButton(previousVote);
    showVoteMessage(`Your current vote: ${getOptionLabelById(pollData, previousVote)}`);
    return;
  }

  showVoteMessage("");
}

function handleVoteClick(event) {
  const selectedVote = event.currentTarget.dataset.vote;

  if (!POLL_OPTION_IDS.includes(selectedVote)) {
    showVoteMessage("Please select a valid option.");
    return;
  }

  const voteResult = registerVote(selectedVote);

  if (!voteResult.isValidVote) {
    showVoteMessage("Your vote could not be recorded. Please try again.");
    return;
  }

  setSelectedButton(selectedVote);
  updateTotalVotesLabel(voteResult.pollData.totalVotes);

  if (!voteResult.changed) {
    showVoteMessage(`Your current vote: ${getOptionLabelById(voteResult.pollData, selectedVote)}`);
    return;
  }

  showVoteMessage(`Vote recorded: ${getOptionLabelById(voteResult.pollData, selectedVote)}`);
}

function setupVoteButtons() {
  DOM.voteButtons.forEach((button) => {
    button.addEventListener("click", handleVoteClick);
  });
}

function cacheDom() {
  DOM.voteButtons = Array.from(document.querySelectorAll("#vote-options .vote-btn"));
  DOM.voteMessage = document.getElementById("voted-msg");
  DOM.totalVotesLabel = document.getElementById("total-votes-label");
}

function init() {
  cacheDom();
  setupVoteButtons();
  hydrateInitialState();
}

document.addEventListener("DOMContentLoaded", init);
