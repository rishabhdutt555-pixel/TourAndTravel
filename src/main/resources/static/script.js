// ============================================
// TOUR & TRAVELS BOOKING SYSTEM
// ============================================

const API = 'http://localhost:8081/api';
let currentUser = null;

// ============================================
// API HELPERS
// ============================================
async function apiFetch(endpoint, options = {}) {
  try {
    const res = await fetch(`${API}${endpoint}`, {
      headers: { 'Content-Type': 'application/json' },
      ...options,
    });
    const data = await res.json();
    if (!res.ok) throw new Error(data.error || 'Request failed');
    return data;
  } catch (err) {
    throw err;
  }
}

// ============================================
// TOAST
// ============================================
function showToast(message, type = 'success') {
  const toast = document.getElementById('toast');
  if (!toast) return;
  toast.textContent = message;
  toast.className = `toast show ${type}`;
  setTimeout(() => toast.classList.remove('show'), 3000);
}

// ============================================
// TAB NAVIGATION
// ============================================
function showTab(tabName) {
  document.querySelectorAll('.tab-btn').forEach(btn => btn.classList.remove('active'));
  if (event && event.target) event.target.classList.add('active');

  document.querySelectorAll('.tab-content').forEach(tab => tab.classList.remove('active'));
  const tabEl = document.getElementById(`${tabName}Tab`);
  if (tabEl) tabEl.classList.add('active');

  if (tabName === 'packages') displayPackages();
  else if (tabName === 'bookings') displayBookings();
  else if (tabName === 'admin') loadAdminStats();
}

// ============================================
// PACKAGES
// ============================================
async function displayPackages() {
  const container = document.getElementById('packagesList');
  if (!container) return;

  const search = document.getElementById('searchInput')?.value || '';
  const sort = document.getElementById('sortBy')?.value || 'price';

  container.innerHTML = `<div class="empty-state"><span>⏳</span><p>Loading packages...</p></div>`;

  try {
    const params = new URLSearchParams({ sort });
    if (search) params.set('search', search);
    const pkgs = await apiFetch(`/packages?${params}`);

    if (pkgs.length === 0) {
      container.innerHTML = `<div class="empty-state"><span>🔍</span><p>No packages found</p></div>`;
      return;
    }

    container.innerHTML = pkgs.map(pkg => `
      <div class="package-card">
        <img src="${pkg.image}" class="package-image" alt="${pkg.name}"
          onerror="this.src='https://placehold.co/400x250/2563eb/white?text=${encodeURIComponent(pkg.name)}'">
        <div class="package-info">
          <div class="package-name">${pkg.name}</div>
          <div class="package-destination">📍 ${pkg.destination}</div>
          <div class="package-details">
            <span class="package-price">₹${pkg.price.toLocaleString()}</span>
            <span class="package-duration">🗓 ${pkg.duration} Days</span>
          </div>
          <div class="package-description">${pkg.description}</div>
          <div style="font-size:0.8rem;color:#94a3b8;margin-bottom:10px;">
            ⭐ ${pkg.rating || 4.0} &nbsp;|&nbsp; 💺 ${pkg.availableSeats} seats left
          </div>
          <button class="book-btn" onclick="bookPackage(${pkg.id})"
            ${!currentUser ? 'disabled title="Login to book"' : ''}>
            ${currentUser ? '✈️ Book Now' : '🔒 Login to Book'}
          </button>
        </div>
      </div>
    `).join('');
  } catch (err) {
    container.innerHTML = `<div class="empty-state"><span>❌</span><p>Failed to load packages: ${err.message}</p></div>`;
  }
}

function filterPackages() { displayPackages(); }
function sortPackages() { displayPackages(); }

// ============================================
// BOOK A PACKAGE
// ============================================
async function bookPackage(packageId) {
  if (!currentUser) {
    showToast('Please login first', 'error');
    return;
  }

  try {
    const booking = await apiFetch('/bookings', {
      method: 'POST',
      body: JSON.stringify({ userId: currentUser.id, packageId }),
    });
    showToast(`✅ Successfully booked ${booking.packageName}!`);
    displayPackages();
    displayBookings();

    // Switch to bookings tab
    document.querySelectorAll('.tab-btn').forEach((btn, i) => {
      btn.classList.toggle('active', i === 1);
    });
    document.querySelectorAll('.tab-content').forEach((tab, i) => {
      tab.classList.toggle('active', i === 1);
    });
    displayBookings();
  } catch (err) {
    showToast(err.message, 'error');
  }
}

// ============================================
// BOOKINGS
// ============================================
async function displayBookings() {
  const container = document.getElementById('bookingsList');
  if (!container) return;

  if (!currentUser) {
    container.innerHTML = `<div class="empty-state"><span>🔒</span><p>Please login to view your bookings</p></div>`;
    return;
  }

  container.innerHTML = `<div class="empty-state"><span>⏳</span><p>Loading bookings...</p></div>`;

  try {
    const bookings = await apiFetch(`/bookings?userId=${currentUser.id}`);

    if (bookings.length === 0) {
      container.innerHTML = `<div class="empty-state"><span>📅</span><p>No bookings yet. Browse packages and book your next adventure!</p></div>`;
      return;
    }

    container.innerHTML = bookings.map(b => `
      <div class="booking-card">
        <div class="booking-info">
          <h4>${b.packageName}</h4>
          <p>📍 ${b.destination} | 🗓 ${b.duration} days | 💰 ₹${b.price.toLocaleString()}</p>
          <p>👥 People: ${b.numberOfPeople} | 💳 Total: ₹${b.totalAmount.toLocaleString()}</p>
          <p>📆 Booked: ${new Date(b.bookingDate).toLocaleDateString()}</p>
          ${b.travelDate ? `<p>✈️ Travel Date: ${b.travelDate}</p>` : ''}
          <span class="booking-status status-${b.status}">✓ ${b.status.toUpperCase()}</span>
          <span style="margin-left:8px;font-size:0.8rem;color:#64748b;">Payment: ${b.paymentStatus}</span>
        </div>
        <button class="cancel-btn" onclick="cancelBooking(${b.id})">Cancel</button>
      </div>
    `).join('');
  } catch (err) {
    container.innerHTML = `<div class="empty-state"><span>❌</span><p>${err.message}</p></div>`;
  }
}

async function cancelBooking(bookingId) {
  if (!confirm('Cancel this booking?')) return;
  try {
    await apiFetch(`/bookings/${bookingId}`, {
      method: 'DELETE',
      body: JSON.stringify({ userId: currentUser.id }),
    });
    showToast('Booking cancelled');
    displayBookings();
    if (currentUser.role === 'admin') loadAdminStats();
  } catch (err) {
    showToast(err.message, 'error');
  }
}

function refreshBookings() { displayBookings(); }

// ============================================
// AUTH
// ============================================
async function login() {
  const username = document.getElementById('loginUsername')?.value;
  const password = document.getElementById('loginPassword')?.value;

  if (!username || !password) {
    showToast('Please enter username and password', 'error');
    return;
  }

  try {
    const data = await apiFetch('/auth/login', {
      method: 'POST',
      body: JSON.stringify({ username, password }),
    });

    currentUser = data.user;
    showToast(`Welcome back, ${currentUser.name}! 🎉`);
    showProfileInfo();
    displayPackages();
    displayBookings();
    if (currentUser.role === 'admin') loadAdminStats();
  } catch (err) {
    showToast(err.message, 'error');
  }
}

async function register() {
  const name = document.getElementById('regName')?.value;
  const email = document.getElementById('regEmail')?.value;
  const phone = document.getElementById('regPhone')?.value;
  const username = document.getElementById('regUsername')?.value;
  const password = document.getElementById('regPassword')?.value;

  if (!name || !email || !phone || !username || !password) {
    showToast('Please fill all fields', 'error');
    return;
  }

  try {
    await apiFetch('/auth/register', {
      method: 'POST',
      body: JSON.stringify({ name, email, phone, username, password }),
    });
    showToast('Registration successful! Please login.');
    showLogin();
  } catch (err) {
    showToast(err.message, 'error');
  }
}

function logout() {
  currentUser = null;
  showLogin();
  document.getElementById('loginUsername').value = '';
  document.getElementById('loginPassword').value = '';
  showToast('Logged out successfully');
  displayPackages();
  displayBookings();
}

function showProfileInfo() {
  document.getElementById('profileInfo').style.display = 'block';
  document.getElementById('loginSection').style.display = 'none';
  document.getElementById('registerSection').style.display = 'none';
  document.getElementById('profileName').textContent = currentUser.name;
  document.getElementById('profileEmail').textContent = currentUser.email;
  document.getElementById('profilePhone').textContent = currentUser.phone || '—';
  document.getElementById('profileUsername').textContent = currentUser.username;
  const roleEl = document.getElementById('profileRole');
  if (roleEl) roleEl.textContent = currentUser.role;
}

function showRegister() {
  document.getElementById('loginSection').style.display = 'none';
  document.getElementById('registerSection').style.display = 'block';
  document.getElementById('profileInfo').style.display = 'none';
}

function showLogin() {
  document.getElementById('loginSection').style.display = 'block';
  document.getElementById('registerSection').style.display = 'none';
  document.getElementById('profileInfo').style.display = 'none';
}

// ============================================
// ADMIN
// ============================================
async function loadAdminStats() {
  if (!currentUser || currentUser.role !== 'admin') return;

  try {
    const stats = await apiFetch(`/stats?userId=${currentUser.id}`);
    document.getElementById('statUsers').textContent = stats.totalUsers;
    document.getElementById('statPackages').textContent = stats.totalPackages;
    document.getElementById('statBookings').textContent = stats.totalBookings;
    const revEl = document.getElementById('statRevenue');
    if (revEl) revEl.textContent = `₹${stats.totalRevenue.toLocaleString()}`;
  } catch (err) {
    console.error('Stats error:', err.message);
  }
}

async function addPackage() {
  if (!currentUser || currentUser.role !== 'admin') {
    showToast('Admin access required', 'error');
    return;
  }

  const name = document.getElementById('packageName')?.value;
  const destination = document.getElementById('packageDestination')?.value;
  const price = parseInt(document.getElementById('packagePrice')?.value);
  const duration = parseInt(document.getElementById('packageDuration')?.value);
  const description = document.getElementById('packageDescription')?.value;
  const image = document.getElementById('packageImage')?.value;

  if (!name || !destination || !price || !duration || !description) {
    showToast('Please fill all required fields', 'error');
    return;
  }

  try {
    await apiFetch('/packages', {
      method: 'POST',
      body: JSON.stringify({ name, destination, price, duration, description, image, userId: currentUser.id }),
    });
    showToast('Package added successfully! 🎉');
    ['packageName','packageDestination','packagePrice','packageDuration','packageDescription','packageImage']
      .forEach(id => { document.getElementById(id).value = ''; });
    displayPackages();
    loadAdminStats();
  } catch (err) {
    showToast(err.message, 'error');
  }
}

// ============================================
// INIT
// ============================================
displayPackages();
