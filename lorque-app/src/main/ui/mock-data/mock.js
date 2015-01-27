exports.mockData = function(req, res, next) {
  switch (req.method) {
    case "GET":
      handleGet(req, res, next);
      break;
    case "POST":
      handlePost(req, res);
      break;
  }
};

function handleGet(req, res, next) {
  // TODO: support API prefix defined in config
  if (req.url.substring(0, 4) === '/api') {
    req.url = req._parsedUrl.pathname + '.json'; // strip query params
    res.setHeader('Content-Type', 'application/json');
  }
  return next();
}

function handlePost(req, res) {
  res.writeHead(200);
  res.end()
}