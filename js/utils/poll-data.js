"use strict";

export const POLL_STORAGE_KEY = "voxgrid_poll_data";
export const USER_VOTE_STORAGE_KEY = "voxgrid_poll_user_vote";
export const POLL_OPTION_IDS = ["yes", "no", "abstain"];

const DEFAULT_POLL_QUESTION =
  "Should City Council meetings enforce a strict 3-minute limit on public comments to allow more speakers?";

const DEFAULT_OPTIONS = [
  {
    id: "yes",
    name: "Yes, prioritize volume of participants",
    votes: 746,
  },
  {
    id: "no",
    name: "No, allow 5 minutes for deeper nuance",
    votes: 373,
  },
  {
    id: "abstain",
    name: "Abstain / See Results",
    votes: 85,
  },
];

function toSafeVoteCount(value, fallback = 0) {
  const parsedValue = Number(value);

  if (!Number.isFinite(parsedValue) || parsedValue < 0) {
    return fallback;
  }

  return Math.floor(parsedValue);
}

function safeGetStorageItem(key) {
  try {
    return localStorage.getItem(key);
  } catch (error) {
    return null;
  }
}

function safeSetStorageItem(key, value) {
  try {
    localStorage.setItem(key, value);
  } catch (error) {
    // Ignore storage write failures to keep the UI functional.
  }
}

function calculateTotalVotes(options) {
  return options.reduce((sum, option) => sum + option.votes, 0);
}

function calculatePercentage(optionVotes, totalVotes) {
  if (totalVotes === 0) {
    return 0;
  }

  return Math.round((optionVotes / totalVotes) * 100);
}

function buildPollData(question, optionsWithVotes) {
  const totalVotes = calculateTotalVotes(optionsWithVotes);
  const options = optionsWithVotes.map((option) => ({
    id: option.id,
    name: option.name,
    votes: option.votes,
    percentage: calculatePercentage(option.votes, totalVotes),
  }));

  return {
    question,
    options,
    totalVotes,
  };
}

function findOptionById(options, optionId) {
  if (!Array.isArray(options)) {
    return null;
  }

  return (
    options.find((option) => String(option?.id).toLowerCase() === optionId) ||
    null
  );
}

function getNormalizedOptionVotes(storedData) {
  return DEFAULT_OPTIONS.map((defaultOption) => {
    const storedOption = findOptionById(storedData?.options, defaultOption.id);

    return {
      id: defaultOption.id,
      name: defaultOption.name,
      votes: toSafeVoteCount(storedOption?.votes, defaultOption.votes),
    };
  });
}

function getSafeQuestion(storedData) {
  const storedQuestion = storedData?.question;

  if (typeof storedQuestion === "string" && storedQuestion.trim()) {
    return storedQuestion.trim();
  }

  return DEFAULT_POLL_QUESTION;
}

export function createDefaultPollData() {
  return buildPollData(DEFAULT_POLL_QUESTION, DEFAULT_OPTIONS);
}

export function loadPollData() {
  const fallbackData = createDefaultPollData();
  const raw = safeGetStorageItem(POLL_STORAGE_KEY);

  if (!raw) {
    return fallbackData;
  }

  try {
    const parsedData = JSON.parse(raw);
    const normalizedOptions = getNormalizedOptionVotes(parsedData);
    const question = getSafeQuestion(parsedData);
    return buildPollData(question, normalizedOptions);
  } catch (error) {
    return fallbackData;
  }
}

export function savePollData(pollData) {
  safeSetStorageItem(POLL_STORAGE_KEY, JSON.stringify(pollData));
}

export function getUserVote() {
  const storedVote = safeGetStorageItem(USER_VOTE_STORAGE_KEY);

  if (!storedVote || !POLL_OPTION_IDS.includes(storedVote)) {
    return null;
  }

  return storedVote;
}

function saveUserVote(voteId) {
  safeSetStorageItem(USER_VOTE_STORAGE_KEY, voteId);
}

function toVoteMap(pollData) {
  const voteMap = {
    yes: 0,
    no: 0,
    abstain: 0,
  };

  pollData.options.forEach((option) => {
    if (POLL_OPTION_IDS.includes(option.id)) {
      voteMap[option.id] = toSafeVoteCount(option.votes, 0);
    }
  });

  return voteMap;
}

export function registerVote(voteId) {
  if (!POLL_OPTION_IDS.includes(voteId)) {
    return {
      pollData: loadPollData(),
      previousVote: getUserVote(),
      changed: false,
      isValidVote: false,
    };
  }

  const currentPollData = loadPollData();
  const previousVote = getUserVote();
  const voteMap = toVoteMap(currentPollData);

  if (previousVote === voteId) {
    return {
      pollData: currentPollData,
      previousVote,
      changed: false,
      isValidVote: true,
    };
  }

  if (previousVote && POLL_OPTION_IDS.includes(previousVote)) {
    voteMap[previousVote] = Math.max(0, voteMap[previousVote] - 1);
  }

  voteMap[voteId] += 1;

  const updatedOptions = DEFAULT_OPTIONS.map((option) => ({
    id: option.id,
    name: option.name,
    votes: voteMap[option.id],
  }));

  const updatedPollData = buildPollData(currentPollData.question, updatedOptions);
  savePollData(updatedPollData);
  saveUserVote(voteId);

  return {
    pollData: updatedPollData,
    previousVote,
    changed: true,
    isValidVote: true,
  };
}
