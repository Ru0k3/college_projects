"use strict";

import {POLL_STORAGE_KEY, loadPollData} from "../utils/poll-data.js";

const RESULT_OPTION_IDS = ["yes", "no", "abstain"];

function getVotesById(pollData) {
  const voteMap = {
    yes: 0,
    no: 0,
    abstain: 0,
  };

  pollData.options.forEach((option) => {
    if (RESULT_OPTION_IDS.includes(option.id)) {
      voteMap[option.id] = option.votes;
    }
  });

  return voteMap;
}

function updateResultRow(option) {
  const pctElement = document.getElementById(`pr-pct-${option.id}`);
  const barElement = document.getElementById(`pr-bar-${option.id}`);
  const countElement = document.getElementById(`pr-count-${option.id}`);

  if (pctElement) {
    pctElement.textContent = `${option.percentage}%`;
  }

  if (barElement) {
    barElement.style.width = `${option.percentage}%`;
  }

  if (countElement) {
    countElement.textContent = `${option.votes} votes`;
  }
}

function getJudgement(votes, totalVotes) {
  if (totalVotes === 0) {
    return "No votes have been recorded yet. Council should wait for broader participation before drawing a procedural conclusion.";
  }

  const yesPct = Math.round((votes.yes / totalVotes) * 100);
  const noPct = Math.round((votes.no / totalVotes) * 100);
  const abstainPct = Math.round((votes.abstain / totalVotes) * 100);

  if (yesPct >= 60) {
    return "A strong majority of residents support enforcing a strict 3-minute limit on public comments at City Council meetings. The community values broader participation and favors a format that allows more voices to be heard within the available session time.";
  }

  if (noPct >= 60) {
    return "A clear majority of residents oppose the 3-minute cap and prefer a 5-minute limit that allows for more substantive public input. The community signals that depth and quality of testimony currently outweigh the volume of speakers.";
  }

  if (abstainPct >= 60) {
    return "A strong majority chose to abstain, signaling uncertainty rather than endorsement of either speaking-time model. Council should gather additional feedback before attempting a procedural change.";
  }

  if (votes.yes > votes.no && votes.yes > votes.abstain) {
    return "Residents are divided, but a plurality lean toward the 3-minute limit. A short pilot period could validate whether this improves participation without reducing input quality.";
  }

  if (votes.no > votes.yes && votes.no > votes.abstain) {
    return "Residents are divided, but a plurality prefer retaining 5 minutes per speaker. More consultation may be helpful before introducing a formal procedural amendment.";
  }

  if (votes.abstain > votes.yes && votes.abstain > votes.no) {
    return "Residents are divided and the largest group abstained. The outcome suggests many participants want more context before choosing a side.";
  }

  return "The community is closely split with no clear mandate. Council should run a structured follow-up process before finalizing any procedural change.";
}

function renderPollResults() {
  const pollData = loadPollData();
  const totalLabel = document.getElementById("pr-total-label");
  const judgementText = document.getElementById("judgement-text");
  const votes = getVotesById(pollData);

  pollData.options.forEach(updateResultRow);

  if (totalLabel) {
    totalLabel.textContent = `${pollData.totalVotes.toLocaleString()} total votes cast`;
  }

  if (judgementText) {
    judgementText.textContent = getJudgement(votes, pollData.totalVotes);
  }
}

document.addEventListener("DOMContentLoaded", renderPollResults);

window.addEventListener("storage", (event) => {
  if (event.key === POLL_STORAGE_KEY) {
    renderPollResults();
  }
});
