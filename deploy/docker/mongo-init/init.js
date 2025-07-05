db = db.getSiblingDB('recommendation');
db.createCollection('recommendation');
db = db.getSiblingDB('matches');
db.createCollection('matches');
