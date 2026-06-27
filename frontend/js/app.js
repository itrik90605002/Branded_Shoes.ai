// Application State Configuration
const API_BASE_URL = 'http://localhost:8080';
let token = localStorage.getItem('jwt_token') || null;
let currentUser = JSON.parse(localStorage.getItem('current_user')) || null;
let productsCache = [];
let verificationUsername = ''; // Holds username during verify flow
let verificationEmail = '';
let verificationContact = '';
let emailVerifiedStatus = false;

// DOM Elements
const btnThemeToggleHome = document.getElementById('btn-theme-toggle-home');
const btnThemeToggleWork = document.getElementById('btn-theme-toggle-work');
const themeBtnText = document.getElementById('theme-btn-text');
const homepageView = document.getElementById('homepage-view');
const authView = document.getElementById('auth-view');
const workspaceView = document.getElementById('workspace-view');
const loginForm = document.getElementById('login-form');
const registerForm = document.getElementById('register-form');
const showRegisterLink = document.getElementById('show-register');
const showLoginLink = document.getElementById('show-login');
const toastEl = document.getElementById('toast');

// Homepage Elements
const homeLoginBtn = document.getElementById('home-login-btn');
const homeRegisterBtn = document.getElementById('home-register-btn');
const btnExploreCollection = document.getElementById('btn-explore-collection');
const btnChatNow = document.getElementById('btn-chat-now');

// Workspace Elements
const userDisplayName = document.getElementById('user-display-name');
const userDisplayRole = document.getElementById('user-display-role');
const btnAdminPortal = document.getElementById('btn-admin-portal');
const btnLogout = document.getElementById('btn-logout');
const chatMessages = document.getElementById('chat-messages');
const chatForm = document.getElementById('chat-form');
const chatInput = document.getElementById('chat-input');

// Workspace Navigation Buttons
const btnBackHome = document.getElementById('btn-back-home');
const btnOpenOrders = document.getElementById('btn-open-orders');
const btnOpenManualOrder = document.getElementById('btn-open-manual-order');

// Track Orders Modal Elements
const ordersModal = document.getElementById('orders-modal');
const btnCloseOrders = document.getElementById('btn-close-orders');
const ordersListContainer = document.getElementById('orders-list-container');
const trackingDetailContainer = document.getElementById('tracking-detail-container');
const btnBackToOrders = document.getElementById('btn-back-to-orders');

// Manual Order Wizard Elements
const manualOrderModal = document.getElementById('manual-order-modal');
const btnCloseManualOrder = document.getElementById('btn-close-manual-order');

const manualStepChooser = document.getElementById('manual-step-chooser');
const manualStepCatalog = document.getElementById('manual-step-catalog');
const manualStepSpecs = document.getElementById('manual-step-specs');

const btnBackToChooser = document.getElementById('btn-back-to-chooser');
const btnBackToCatalog = document.getElementById('btn-back-to-catalog');

const manualCatalogHeader = document.getElementById('manual-catalog-header');
const manualCatalogGrid = document.getElementById('manual-catalog-grid');

const manualSpecItemImg = document.getElementById('manual-spec-item-img');
const manualSpecItemName = document.getElementById('manual-spec-item-name');
const manualSpecBrand = document.getElementById('manual-spec-brand');

const manualSelectColor = document.getElementById('manual-select-color');
const manualSelectSize = document.getElementById('manual-select-size');
const manualAvailabilityCard = document.getElementById('manual-availability-card');
const manualAvailabilityBadge = document.getElementById('manual-availability-badge');
const manualItemPrice = document.getElementById('manual-item-price');
const btnManualOrderBuy = document.getElementById('btn-manual-order-buy');

let currentManualProduct = null;
let selectedWizardCategory = '';
let selectedWizardModel = '';
let selectedWizardBrand = '';

// Hidable Chat History Elements
const workspaceGrid = document.getElementById('workspace-grid');
const btnToggleHistory = document.getElementById('btn-toggle-history');
const btnClearHistory = document.getElementById('btn-clear-history');
const historyList = document.getElementById('history-list');

// Catalog Drawer Elements
const btnToggleCatalog = document.getElementById('btn-toggle-catalog');
const btnCloseCatalog = document.getElementById('btn-close-catalog');
const catalogDrawer = document.getElementById('catalog-drawer');
const drawerOverlay = document.getElementById('drawer-overlay');
const catalogList = document.getElementById('catalog-list');
const filterBrand = document.getElementById('filter-brand');
const filterColor = document.getElementById('filter-color');
const filterSize = document.getElementById('filter-size');

// Admin Portal Elements
const btnAdminPortalNav = document.getElementById('btn-admin-portal');
const btnCloseAdmin = document.getElementById('btn-close-admin');
const adminModal = document.getElementById('admin-modal');
const adminAddProductForm = document.getElementById('admin-add-product-form');
const adminInventoryList = document.getElementById('admin-inventory-list');

// Danger Zone & Confirmation Modals
const btnDeleteAccount = document.getElementById('btn-delete-account');
const confirmModal = document.getElementById('confirm-modal');
const confirmDeleteCancel = document.getElementById('confirm-delete-cancel');
const confirmDeleteOk = document.getElementById('confirm-delete-ok');

// Verification Modal Elements
const verifyModal = document.getElementById('verify-modal');
const verifyEmailCode = document.getElementById('verify-email-code');
const btnVerifyEmail = document.getElementById('btn-verify-email');
const btnResendCodes = document.getElementById('btn-resend-codes');
const btnVerificationClose = document.getElementById('btn-verification-close');
const emailVerifyStatus = document.getElementById('email-verify-status');

// Google Sign-In Elements
const btnGoogleSignin = document.getElementById('btn-google-signin');
const googleChooserModal = document.getElementById('google-chooser-modal');
const btnCloseGoogle = document.getElementById('btn-close-google');
const googleAccountItems = document.querySelectorAll('.google-account-item');

// User Profile Elements
const btnOpenProfile = document.getElementById('btn-open-profile');
const profileModal = document.getElementById('profile-modal');
const btnCloseProfile = document.getElementById('btn-close-profile');
const profileUsername = document.getElementById('profile-username');
const profileEmail = document.getElementById('profile-email');
const profileContact = document.getElementById('profile-contact');
const profileRoleBadge = document.getElementById('profile-role-badge');
const btnEditProfile = document.getElementById('btn-edit-profile');
const btnSaveProfile = document.getElementById('btn-save-profile');
const btnCancelProfile = document.getElementById('btn-cancel-profile');

// Secure Checkout & Payment Elements
const checkoutModal = document.getElementById('checkout-modal');
const btnCloseCheckout = document.getElementById('btn-close-checkout');
const btnGotoPayment = document.getElementById('btn-goto-payment');
const btnBackToShipping = document.getElementById('btn-back-to-shipping');
const btnCompleteOrder = document.getElementById('btn-complete-order');
const btnCloseInvoice = document.getElementById('btn-close-invoice');
const btnVerifyUpi = document.getElementById('btn-verify-upi');

const checkoutStep1 = document.getElementById('checkout-step-1');
const checkoutStep2 = document.getElementById('checkout-step-2');
const checkoutStep3 = document.getElementById('checkout-step-3');

const checkoutProdName = document.getElementById('checkout-prod-name');
const checkoutProdBrand = document.getElementById('checkout-prod-brand');
const checkoutProdSpecs = document.getElementById('checkout-prod-specs');
const checkoutProdPrice = document.getElementById('checkout-prod-price');

const checkoutAddress = document.getElementById('checkout-address');
const checkoutContact = document.getElementById('checkout-contact');
const inputUpiId = document.getElementById('input-upi-id');

const paymentQrArea = document.getElementById('payment-qr-area');
const paymentUpiArea = document.getElementById('payment-upi-area');
const paymentPodArea = document.getElementById('payment-pod-area');
const paymentRedirectingScreen = document.getElementById('payment-redirecting-screen');

const qrCodeImg = document.getElementById('qr-code-img');
const invoiceId = document.getElementById('invoice-id');
const invoiceItemName = document.getElementById('invoice-item-name');
const invoiceItemSpecs = document.getElementById('invoice-item-specs');
const invoicePaymentMethod = document.getElementById('invoice-payment-method');
const invoiceItemPrice = document.getElementById('invoice-item-price');

let currentCheckoutProduct = null;

// Initialize App
document.addEventListener('DOMContentLoaded', () => {
    setupAuthEventListeners();
    setupWorkspaceEventListeners();
    setupVerificationEventListeners();
    setupHomepageEventListeners();
    setupGoogleLoginEventListeners();
    setupUserProfileEventListeners();
    setupCheckoutEventListeners();
    setupTrackingEventListeners();
    setupManualOrderEventListeners();
    
    // Initialize theme settings
    const savedTheme = localStorage.getItem('theme') || 'light';
    if (savedTheme === 'dark') {
        document.body.classList.add('dark-theme');
        updateThemeToggleUI(true);
    } else {
        updateThemeToggleUI(false);
    }

    showHomepage();
});

// ================= THEME TOGGLE CONTROL =================
function toggleTheme() {
    const isDark = document.body.classList.toggle('dark-theme');
    localStorage.setItem('theme', isDark ? 'dark' : 'light');
    updateThemeToggleUI(isDark);
}

function updateThemeToggleUI(isDark) {
    if (isDark) {
        btnThemeToggleHome.innerHTML = '<i class="fa-solid fa-sun"></i>';
        btnThemeToggleWork.innerHTML = '<i class="fa-solid fa-sun"></i> <span id="theme-btn-text">Light Mode</span>';
    } else {
        btnThemeToggleHome.innerHTML = '<i class="fa-solid fa-moon"></i>';
        btnThemeToggleWork.innerHTML = '<i class="fa-solid fa-moon"></i> <span id="theme-btn-text">Dark Mode</span>';
    }
}

// Toast Notifications Helper
function showToast(message, type = 'info') {
    toastEl.textContent = message;
    toastEl.className = `toast ${type}`;
    toastEl.classList.remove('hidden');
    
    setTimeout(() => {
        toastEl.classList.add('hidden');
    }, 4500);
}

// ================= NAVIGATION ROUTING =================
function showHomepage() {
    document.body.classList.remove('workspace-active');
    homepageView.classList.remove('hidden');
    authView.classList.add('hidden');
    workspaceView.classList.add('hidden');
}

// Subview transitions
function showAuth(subview = 'login') {
    document.body.classList.remove('workspace-active');
    homepageView.classList.add('hidden');
    authView.classList.remove('hidden');
    workspaceView.classList.add('hidden');
    
    if (subview === 'register') {
        loginForm.classList.add('hidden');
        registerForm.classList.remove('hidden');
        document.querySelector('.auth-header h2').textContent = 'Create Account';
    } else {
        registerForm.classList.add('hidden');
        loginForm.classList.remove('hidden');
        document.querySelector('.auth-header h2').textContent = 'Welcome Back';
    }
}

function showWorkspace() {
    document.body.classList.add('workspace-active');
    homepageView.classList.add('hidden');
    authView.classList.add('hidden');
    workspaceView.classList.remove('hidden');
    
    userDisplayName.textContent = currentUser.username;
    userDisplayRole.textContent = currentUser.role;
    userDisplayRole.className = `badge ${currentUser.role === 'ADMIN' ? 'badge-purple' : 'badge-indigo'}`;

    if (currentUser.role === 'ADMIN') {
        btnAdminPortal.classList.remove('hidden');
    } else {
        btnAdminPortal.classList.add('hidden');
    }

    chatMessages.innerHTML = `
        <div class="msg msg-ai">
            <div class="avatar"><i class="fa-solid fa-robot"></i></div>
            <div class="msg-content">
                <p>Welcome back, ${currentUser.username}! I am your AI Shopkeeper running our store. We sell top-brand shoes: Campus, Nike, Skechers, Puma, and Sparx. Ask me for specific colors, brands, and sizes, or track your orders!</p>
            </div>
        </div>
    `;
    
    renderChatHistoryList();

    fetchCatalogFilters();
    fetchCatalogProducts();
}

// ================= HOMEPAGE HANDLERS =================
function setupHomepageEventListeners() {
    btnThemeToggleHome.addEventListener('click', toggleTheme);
    homeLoginBtn.addEventListener('click', () => showAuth('login'));
    homeRegisterBtn.addEventListener('click', () => showAuth('register'));
    
    btnExploreCollection.addEventListener('click', () => {
        document.getElementById('collection-section').scrollIntoView({ behavior: 'smooth' });
    });
    
    btnChatNow.addEventListener('click', () => {
        if (token && currentUser) {
            showWorkspace();
        } else {
            showToast('Please log in or register to talk with our AI Shopkeeper!', 'info');
            showAuth('login');
        }
    });
}

// ================= AUTH MANAGEMENT =================
function setupAuthEventListeners() {
    showRegisterLink.addEventListener('click', (e) => {
        e.preventDefault();
        showAuth('register');
    });

    showLoginLink.addEventListener('click', (e) => {
        e.preventDefault();
        showAuth('login');
    });

    // Handle Login Submit
    loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const username = document.getElementById('login-username').value.trim();
        const password = document.getElementById('login-password').value;

        try {
            const response = await fetch(`${API_BASE_URL}/api/auth/login`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username, password })
            });

            const data = await response.json();

            if (response.ok) {
                token = data.token;
                currentUser = {
                    username: data.username,
                    email: data.email,
                    role: data.role
                };
                localStorage.setItem('jwt_token', token);
                localStorage.setItem('current_user', JSON.stringify(currentUser));
                showToast('Login successful!', 'success');
                showWorkspace();
            } else {
                showToast(data.message || 'Login failed. Check credentials.', 'error');
                if (data.message && data.message.includes('verification pending')) {
                    verificationUsername = username;
                    openVerificationModal();
                }
            }
        } catch (error) {
            showToast('Network error connecting to backend.', 'error');
            console.error('Login error:', error);
        }
    });

    // Handle Register Submit
    registerForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const username = document.getElementById('reg-username').value.trim();
        const email = document.getElementById('reg-email').value.trim();
        const contactNumber = document.getElementById('reg-contact').value.trim();
        const password = document.getElementById('reg-password').value;
        const role = document.getElementById('reg-role').value;

        try {
            const response = await fetch(`${API_BASE_URL}/api/auth/register`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username, email, contactNumber, password, role })
            });

            const data = await response.json();

            if (response.ok) {
                showToast('Registration successful! Verification codes dispatched.', 'success');
                verificationUsername = username;
                verificationEmail = email;
                verificationContact = contactNumber;
                openVerificationModal();
            } else {
                showToast(data.message || 'Registration failed.', 'error');
            }
        } catch (error) {
            showToast('Network error connecting to backend.', 'error');
            console.error('Registration error:', error);
        }
    });
}

// ================= GOOGLE AUTH MOCK HANDLERS =================
function setupGoogleLoginEventListeners() {
    btnGoogleSignin.addEventListener('click', () => {
        googleChooserModal.classList.remove('hidden');
    });

    btnCloseGoogle.addEventListener('click', () => {
        googleChooserModal.classList.add('hidden');
    });

    googleAccountItems.forEach(item => {
        item.addEventListener('click', async () => {
            const email = item.getAttribute('data-email');
            const name = item.getAttribute('data-name');
            googleChooserModal.classList.add('hidden');

            try {
                const response = await fetch(`${API_BASE_URL}/api/auth/google-login?email=${encodeURIComponent(email)}&name=${encodeURIComponent(name)}`, {
                    method: 'POST'
                });
                const data = await response.json();

                if (response.ok) {
                    token = data.token;
                    currentUser = {
                        username: data.username,
                        email: data.email,
                        role: data.role
                    };
                    localStorage.setItem('jwt_token', token);
                    localStorage.setItem('current_user', JSON.stringify(currentUser));
                    showToast(`Logged in with Google as ${name}!`, 'success');
                    showWorkspace();
                } else {
                    showToast(data.message || 'Google Sign-in failed.', 'error');
                }
            } catch (error) {
                showToast('Network error connecting to Google Auth Endpoint.', 'error');
            }
        });
    });
}

// ================= USER PROFILE HANDLERS =================
function setupUserProfileEventListeners() {
    btnOpenProfile.addEventListener('click', openUserProfileModal);
    btnCloseProfile.addEventListener('click', () => profileModal.classList.add('hidden'));

    btnEditProfile.addEventListener('click', () => {
        profileEmail.disabled = false;
        profileContact.disabled = false;
        profileEmail.focus();

        btnEditProfile.classList.add('hidden');
        btnSaveProfile.classList.remove('hidden');
        btnCancelProfile.classList.remove('hidden');
    });

    btnCancelProfile.addEventListener('click', () => {
        disableProfileFields();
        openUserProfileModal(); // Refresh details to original
    });

    btnSaveProfile.addEventListener('click', async () => {
        const email = profileEmail.value.trim();
        const contact = profileContact.value.trim();

        if (!email || !contact) {
            showToast('Email and Contact details are required!', 'error');
            return;
        }

        try {
            const response = await fetch(`${API_BASE_URL}/api/auth/profile?email=${encodeURIComponent(email)}&contactNumber=${encodeURIComponent(contact)}`, {
                method: 'PUT',
                headers: { 'Authorization': `Bearer ${token}` }
            });
            const data = await response.json();

            if (response.ok) {
                showToast('Profile updated successfully!', 'success');
                currentUser.email = data.email;
                localStorage.setItem('current_user', JSON.stringify(currentUser));
                disableProfileFields();
                openUserProfileModal();
            } else {
                showToast(data.message || 'Failed updating profile.', 'error');
            }
        } catch (e) {
            showToast('Network error updating profile.', 'error');
        }
    });

    // Delete Account Danger Zone Trigger
    btnDeleteAccount.addEventListener('click', () => {
        profileModal.classList.add('hidden');
        confirmModal.classList.remove('hidden');
    });
}

async function openUserProfileModal() {
    try {
        const response = await fetch(`${API_BASE_URL}/api/auth/profile`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        if (response.ok) {
            const user = await response.json();
            profileUsername.value = user.username;
            profileEmail.value = user.email || '';
            profileContact.value = user.contactNumber || '';
            
            profileRoleBadge.textContent = user.role;
            profileRoleBadge.className = `badge ${user.role === 'ADMIN' ? 'badge-purple' : 'badge-indigo'}`;
            
            disableProfileFields();
            profileModal.classList.remove('hidden');
        } else {
            showToast('Failed to fetch profile details.', 'error');
        }
    } catch (e) {
        showToast('Network error loading profile.', 'error');
    }
}

function disableProfileFields() {
    profileEmail.disabled = true;
    profileContact.disabled = true;
    btnEditProfile.classList.remove('hidden');
    btnSaveProfile.classList.add('hidden');
    btnCancelProfile.classList.add('hidden');
}

// ================= EMAIL VERIFICATION FLOW HANDLERS =================
function setupVerificationEventListeners() {
    btnVerifyEmail.addEventListener('click', async () => {
        const code = verifyEmailCode.value.trim();
        if (!code) {
            showToast('Please enter the email verification code.', 'error');
            return;
        }

        try {
            const response = await fetch(`${API_BASE_URL}/api/auth/verify-email?username=${encodeURIComponent(verificationUsername)}&code=${encodeURIComponent(code)}`);
            const data = await response.json();

            if (response.ok) {
                showToast('Email verified successfully!', 'success');
                emailVerifiedStatus = true;
                emailVerifyStatus.innerHTML = '<i class="fa-solid fa-circle-check" style="color:var(--color-success);"></i> Email Verified!';
                checkVerificationSuccess();
            } else {
                showToast(data.message || 'Verification failed.', 'error');
            }
        } catch (e) {
            showToast('Network error verifying email.', 'error');
        }
    });

    btnResendCodes.addEventListener('click', async () => {
        try {
            const response = await fetch(`${API_BASE_URL}/api/auth/resend-codes?username=${encodeURIComponent(verificationUsername)}`, {
                method: 'POST'
            });
            const data = await response.json();

            if (response.ok) {
                showToast('Verification code resent successfully!', 'success');
            } else {
                showToast(data.message || 'Failed resending code.', 'error');
            }
        } catch (e) {
            showToast('Network error resending verification code.', 'error');
        }
    });

    btnVerificationClose.addEventListener('click', () => {
        verifyModal.classList.add('hidden');
        showAuth('login');
    });
}

function openVerificationModal() {
    verifyEmailCode.value = '';
    emailVerifiedStatus = false;
    emailVerifyStatus.innerHTML = '<i class="fa-solid fa-circle-question"></i> Pending verification';
    verifyModal.classList.remove('hidden');
}

function checkVerificationSuccess() {
    if (emailVerifiedStatus) {
        setTimeout(() => {
            showToast('Account activated! You can now log in.', 'success');
            verifyModal.classList.add('hidden');
            registerForm.reset();
            showAuth('login');
        }, 1000);
    }
}

btnLogout.addEventListener('click', () => {
    localStorage.removeItem('jwt_token');
    localStorage.removeItem('current_user');
    token = null;
    currentUser = null;
    showToast('Logged out successfully.', 'info');
    showHomepage();
});


// ================= WORKSPACE INTERACTIVE BEHAVIORS =================
function setupWorkspaceEventListeners() {
    btnThemeToggleWork.addEventListener('click', toggleTheme);
    btnToggleCatalog.addEventListener('click', () => {
        catalogDrawer.classList.add('active');
        drawerOverlay.classList.remove('hidden');
    });

    btnCloseCatalog.addEventListener('click', closeCatalog);
    drawerOverlay.addEventListener('click', closeCatalog);

    btnAdminPortalNav.addEventListener('click', () => {
        adminModal.classList.remove('hidden');
        renderAdminInventoryTable();
    });
    btnCloseAdmin.addEventListener('click', () => {
        adminModal.classList.add('hidden');
    });

    confirmDeleteCancel.addEventListener('click', () => {
        confirmModal.classList.add('hidden');
    });
    confirmDeleteOk.addEventListener('click', deleteUserAccount);

    filterBrand.addEventListener('change', renderCatalogProducts);
    filterColor.addEventListener('change', renderCatalogProducts);
    filterSize.addEventListener('change', renderCatalogProducts);

    document.querySelectorAll('.chip').forEach(chip => {
        chip.addEventListener('click', () => {
            chatInput.value = chip.getAttribute('data-prompt');
            chatInput.focus();
        });
    });

    chatForm.addEventListener('submit', handleChatSubmit);

    // Hidable history events
    btnToggleHistory.addEventListener('click', () => {
        workspaceGrid.classList.toggle('history-hidden');
    });

    btnClearHistory.addEventListener('click', async () => {
        localStorage.removeItem('chat_history_' + currentUser.username);
        renderChatHistoryList();
        
        chatMessages.innerHTML = `
            <div class="msg msg-ai">
                <div class="avatar"><i class="fa-solid fa-robot"></i></div>
                <div class="msg-content">
                    <p>Welcome back, ${currentUser.username}! I am your AI Shopkeeper running our store. We sell top-brand shoes: Campus, Nike, Skechers, Puma, and Sparx. Ask me for specific colors, brands, and sizes, or track your orders!</p>
                </div>
            </div>
        `;
        
        try {
            await fetch(`${API_BASE_URL}/api/agent/clear-history`, {
                method: 'POST',
                headers: { 'Authorization': `Bearer ${token}` }
            });
        } catch (e) {
            console.error('Failed clear history:', e);
        }
        showToast('Chat history cleared.', 'success');
    });
}

function closeCatalog() {
    catalogDrawer.classList.remove('active');
    drawerOverlay.classList.add('hidden');
}

// ================= CHAT HISTORY PANEL RENDER =================
function addChatToHistoryList(query, reply) {
    let history = JSON.parse(localStorage.getItem('chat_history_' + currentUser.username)) || [];
    history.push({ query, reply, timestamp: new Date().toLocaleTimeString() });
    localStorage.setItem('chat_history_' + currentUser.username, JSON.stringify(history));
    renderChatHistoryList();
}

function renderChatHistoryList() {
    if (!currentUser) return;
    let history = JSON.parse(localStorage.getItem('chat_history_' + currentUser.username)) || [];
    
    if (history.length === 0) {
        historyList.innerHTML = `
            <div class="no-history">
                <i class="fa-solid fa-clock-rotate-left"></i>
                <p>No chat history in this session yet.</p>
            </div>
        `;
        return;
    }

    historyList.innerHTML = '';
    // Display in reverse chronological order
    for (let i = history.length - 1; i >= 0; i--) {
        const item = history[i];
        const div = document.createElement('div');
        div.className = 'history-item';
        div.innerHTML = `
            <div class="history-item-query"><i class="fa-solid fa-circle-question text-indigo"></i> ${item.query}</div>
            <div class="history-item-reply"><i class="fa-solid fa-reply text-purple"></i> ${item.reply}</div>
        `;
        div.addEventListener('click', () => {
            chatInput.value = item.query;
            chatInput.focus();
        });
        historyList.appendChild(div);
    }
}

// ================= SECURE CHECKOUT FLOW EVENTS =================
function setupCheckoutEventListeners() {
    btnCloseCheckout.addEventListener('click', () => checkoutModal.classList.add('hidden'));
    btnCloseInvoice.addEventListener('click', () => checkoutModal.classList.add('hidden'));
    
    btnGotoPayment.addEventListener('click', () => {
        const address = checkoutAddress.value.trim();
        const contact = checkoutContact.value.trim();
        
        if (!address || !contact) {
            showToast('Please fill in your shipping details.', 'error');
            return;
        }
        
        // Update price labels
        document.querySelectorAll('.pay-amount-label').forEach(el => {
            el.textContent = currentCheckoutProduct.price.toFixed(2);
        });
        
        // Generate UPI Dynamic QR code
        const upiUri = `upi://pay?pa=mandelbulbshop@upi&pn=SHOE.AI&am=${currentCheckoutProduct.price.toFixed(2)}&cu=INR`;
        qrCodeImg.src = `https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=${encodeURIComponent(upiUri)}`;
        
        checkoutStep1.classList.add('hidden');
        checkoutStep2.classList.remove('hidden');
    });

    btnBackToShipping.addEventListener('click', () => {
        checkoutStep2.classList.add('hidden');
        checkoutStep1.classList.remove('hidden');
    });

    document.querySelectorAll('input[name="payment-method"]').forEach(radio => {
        radio.addEventListener('change', (e) => {
            paymentQrArea.classList.add('hidden');
            paymentUpiArea.classList.add('hidden');
            paymentPodArea.classList.add('hidden');
            paymentRedirectingScreen.classList.add('hidden');
            btnCompleteOrder.classList.remove('hidden');
            
            const selected = e.target.value;
            if (selected === 'QR') {
                paymentQrArea.classList.remove('hidden');
            } else if (selected === 'UPI') {
                paymentUpiArea.classList.remove('hidden');
            } else if (selected === 'POD') {
                paymentPodArea.classList.remove('hidden');
            }
        });
    });

    btnVerifyUpi.addEventListener('click', () => {
        const upiId = inputUpiId.value.trim();
        if (!upiId || !upiId.includes('@')) {
            showToast('Please enter a valid UPI ID (e.g. name@upi).', 'error');
            return;
        }
        showToast('UPI ID verified successfully!', 'success');
    });

    // Direct UPI App Redirection Trigger
    document.querySelectorAll('.btn-upi-app').forEach(btn => {
        btn.addEventListener('click', () => {
            const appName = btn.getAttribute('data-app').toUpperCase();
            
            paymentUpiArea.classList.add('hidden');
            paymentRedirectingScreen.classList.remove('hidden');
            btnCompleteOrder.classList.add('hidden');
            document.getElementById('redirect-app-title').textContent = `Opening ${appName} App...`;
            
            // Redirect OS to payment app in reality!
            const upiLink = `upi://pay?pa=mandelbulbshop@upi&pn=SHOE.AI&am=${currentCheckoutProduct.price.toFixed(2)}&cu=INR`;
            window.location.href = upiLink;
            
            // Simulate direct payment app authentication response (redirect back to success page after 4.5s)
            setTimeout(async () => {
                await processFinalCheckoutOrder('PAID (UPI ' + appName + ')');
            }, 4500);
        });
    });

    btnCompleteOrder.addEventListener('click', async () => {
        const method = document.querySelector('input[name="payment-method"]:checked').value;
        let paymentLabel = 'PENDING (PAY ON DELIVERY)';
        
        if (method === 'QR') {
            paymentLabel = 'PAID (QR SCAN)';
        } else if (method === 'UPI') {
            const upiId = inputUpiId.value.trim();
            if (!upiId || !upiId.includes('@')) {
                showToast('Please enter and verify your UPI ID first.', 'error');
                return;
            }
            paymentLabel = 'PAID (UPI: ' + upiId + ')';
        }
        
        await processFinalCheckoutOrder(paymentLabel);
    });
}

function openCheckoutFlow(product) {
    currentCheckoutProduct = product;
    
    checkoutProdName.textContent = product.name;
    checkoutProdBrand.textContent = product.brand;
    checkoutProdSpecs.textContent = `Size: ${product.size} | Color: ${product.color}`;
    checkoutProdPrice.textContent = `₹${product.price.toFixed(2)}`;
    
    checkoutContact.value = profileContact.value || '';
    checkoutAddress.value = '';
    inputUpiId.value = '';
    
    // reset steps
    checkoutStep1.classList.remove('hidden');
    checkoutStep2.classList.add('hidden');
    checkoutStep3.classList.add('hidden');
    
    paymentQrArea.classList.remove('hidden');
    paymentUpiArea.classList.add('hidden');
    paymentPodArea.classList.add('hidden');
    paymentRedirectingScreen.classList.add('hidden');
    btnCompleteOrder.classList.remove('hidden');
    
    document.querySelector('input[name="payment-method"][value="QR"]').checked = true;
    
    checkoutModal.classList.remove('hidden');
}

async function processFinalCheckoutOrder(paymentMethod) {
    try {
        const response = await fetch(`${API_BASE_URL}/api/orders?productId=${currentCheckoutProduct.productId}&paymentStatus=${encodeURIComponent(paymentMethod)}`, {
            method: 'POST',
            headers: { 'Authorization': `Bearer ${token}` }
        });
        
        if (response.ok) {
            const order = await response.json();
            
            invoiceId.textContent = order.orderId;
            invoiceItemName.textContent = currentCheckoutProduct.name;
            invoiceItemSpecs.textContent = `Size ${currentCheckoutProduct.size} | Color ${currentCheckoutProduct.color} | Brand ${currentCheckoutProduct.brand}`;
            invoicePaymentMethod.textContent = paymentMethod;
            invoiceItemPrice.textContent = `₹${currentCheckoutProduct.price.toFixed(2)}`;
            
            checkoutStep2.classList.add('hidden');
            checkoutStep3.classList.remove('hidden');
            
            fetchCatalogProducts();
            showToast('Order placed successfully!', 'success');
        } else {
            const data = await response.json();
            showToast(data.message || 'Failed placing order.', 'error');
            checkoutModal.classList.add('hidden');
        }
    } catch (e) {
        showToast('Network error placing order.', 'error');
        checkoutModal.classList.add('hidden');
    }
}

// ================= ORDER TRACKING AND STEPPER EVENT LISTENERS =================
function setupTrackingEventListeners() {
    // Nav to home button
    btnBackHome.addEventListener('click', (e) => {
        e.preventDefault();
        showHomepage();
    });

    // Toggle track orders modal
    btnOpenOrders.addEventListener('click', () => {
        ordersModal.classList.remove('hidden');
        trackingDetailContainer.classList.add('hidden');
        ordersListContainer.classList.remove('hidden');
        fetchUserOrders();
    });

    btnCloseOrders.addEventListener('click', () => {
        ordersModal.classList.add('hidden');
    });

    btnBackToOrders.addEventListener('click', () => {
        trackingDetailContainer.classList.add('hidden');
        ordersListContainer.classList.remove('hidden');
    });
}

async function fetchUserOrders() {
    try {
        const response = await fetch(`${API_BASE_URL}/api/orders`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        
        if (response.ok) {
            const orders = await response.json();
            renderOrdersList(orders);
        } else {
            showToast('Failed to retrieve order history.', 'error');
        }
    } catch (e) {
        showToast('Network error loading order tracking list.', 'error');
    }
}

function renderOrdersList(orders) {
    if (orders.length === 0) {
        ordersListContainer.innerHTML = `
            <div style="text-align:center; padding:30px; color:var(--text-secondary);">
                <i class="fa-solid fa-folder-open" style="font-size:2.5rem; margin-bottom:12px; color:var(--text-muted);"></i>
                <p>You have not placed any orders yet.</p>
            </div>
        `;
        return;
    }

    ordersListContainer.innerHTML = '';
    orders.forEach(order => {
        // Find product name in cache if available
        const product = productsCache.find(p => p.productId === order.productId) || { name: 'Premium Shoes', brand: 'SHOE.AI' };
        
        const card = document.createElement('div');
        card.className = 'order-item-card';
        card.style.marginBottom = '10px';
        card.innerHTML = `
            <div>
                <h4 style="margin:0 0 4px 0; font-weight:700; color:var(--text-primary); font-size:0.9rem;">${order.orderId}</h4>
                <div style="font-size:0.75rem; color:var(--text-secondary);">${product.brand} ${product.name}</div>
            </div>
            <div style="text-align:right;">
                <span class="badge ${order.status.toLowerCase() === 'delivered' ? 'badge-success' : 'badge-indigo'}" style="font-size:0.65rem;">
                    ${order.status.toUpperCase()}
                </span>
                <div style="font-size:0.7rem; color:var(--text-muted); margin-top:4px;">Delivery: ${order.deliveryDate}</div>
            </div>
        `;
        
        card.addEventListener('click', () => showTrackingDetail(order, product));
        ordersListContainer.appendChild(card);
    });
}

function showTrackingDetail(order, product) {
    ordersListContainer.classList.add('hidden');
    trackingDetailContainer.classList.remove('hidden');

    document.getElementById('track-order-id').textContent = `Order ${order.orderId}`;
    document.getElementById('track-order-item').textContent = `${product.brand} ${product.name} | Specs: Size ${product.size || 8} | Color ${product.color || 'Default'}`;
    document.getElementById('track-order-delivery').textContent = `Expected Delivery: ${order.deliveryDate}`;

    // Update payment verification label
    const payStatusEl = document.getElementById('track-payment-status');
    const isPaid = order.paymentStatus ? order.paymentStatus.toUpperCase().includes('PAID') : true;
    payStatusEl.textContent = `Payment Status: ${order.paymentStatus || 'PAID (VERIFIED)'}`;
    
    // Reset steps classes
    const steps = ['placed', 'paid', 'dispatched', 'out', 'delivered'];
    steps.forEach(step => {
        const el = document.getElementById(`step-${step}`);
        el.className = 'step-item';
    });

    // Step 1: Placed is always completed
    document.getElementById('step-placed').classList.add('completed');

    // Step 2: Payment status check
    if (isPaid) {
        document.getElementById('step-paid').classList.add('completed');
    } else {
        document.getElementById('step-paid').classList.add('active'); // active pending
    }

    // Step 3, 4, 5 based on shipping status
    const status = order.status.toLowerCase();
    
    if (status === 'processing') {
        if (isPaid) {
            document.getElementById('step-dispatched').classList.add('active');
        }
    } else if (status === 'shipped' || status === 'dispatched') {
        document.getElementById('step-paid').classList.add('completed');
        document.getElementById('step-dispatched').classList.add('completed');
        document.getElementById('step-out').classList.add('active');
    } else if (status === 'out_for_delivery' || status === 'out') {
        document.getElementById('step-paid').classList.add('completed');
        document.getElementById('step-dispatched').classList.add('completed');
        document.getElementById('step-out').classList.add('completed');
        document.getElementById('step-delivered').classList.add('active');
    } else if (status === 'delivered') {
        document.getElementById('step-paid').classList.add('completed');
        document.getElementById('step-dispatched').classList.add('completed');
        document.getElementById('step-out').classList.add('completed');
        document.getElementById('step-delivered').classList.add('completed');
    }
}

// ================= FOOTWEAR IMAGES MAPPING HELPER =================
function getCategoryImgUrl(category) {
    const cat = category.toLowerCase();
    if (cat.includes('slipper')) return 'images/comfy_slippers_1782547527812.png';
    if (cat.includes('sandal')) return 'images/premium_sandals_1782566246833.png';
    if (cat.includes('heels') || cat.includes('party')) return 'images/luxury_heels_1782547492181.png';
    if (cat.includes('long boots')) return 'images/leather_boots_1782547510857.png';
    if (cat.includes('short boots') || cat.includes('boots')) return 'images/leather_boots_1782547510857.png';
    return 'images/premium_shoes_1782547476439.png'; // default shoes
}

// ================= MANUAL ORDER WIZARD HANDLERS =================
function setupManualOrderEventListeners() {
    // Open modal to Step 1 (Chooser)
    btnOpenManualOrder.addEventListener('click', () => {
        manualOrderModal.classList.remove('hidden');
        showWizardStep(manualStepChooser);
    });

    btnCloseManualOrder.addEventListener('click', () => {
        manualOrderModal.classList.add('hidden');
    });

    // Step 1: Click Category cards
    document.querySelectorAll('.footwear-cat-card').forEach(card => {
        card.addEventListener('click', () => {
            const category = card.getAttribute('data-category');
            selectedWizardCategory = category;
            
            // Render matching catalog products
            manualCatalogHeader.textContent = `Available ${category} in Stock`;
            renderWizardCatalog(category);
            
            showWizardStep(manualStepCatalog);
        });
    });

    // Back Buttons
    btnBackToChooser.addEventListener('click', () => {
        showWizardStep(manualStepChooser);
    });

    btnBackToCatalog.addEventListener('click', () => {
        showWizardStep(manualStepCatalog);
    });

    // Color Dropdown Change in Step 3
    manualSelectColor.addEventListener('change', () => {
        const color = manualSelectColor.value;
        if (!color) {
            resetWizardSelect(manualSelectSize);
            hideWizardAvailability();
            return;
        }

        // Filter unique sizes for selected model and color
        const sizes = [...new Set(productsCache.filter(p => 
            p.category === selectedWizardCategory && 
            p.brand === selectedWizardBrand && 
            p.name === selectedWizardModel && 
            p.color === color
        ).map(p => p.size))];
        
        populateWizardSelect(manualSelectSize, sizes, '-- Choose Size --');
        hideWizardAvailability();
    });

    // Size Dropdown Change in Step 3
    manualSelectSize.addEventListener('change', checkWizardProductAvailability);

    // Final Buy Now Button Click in Step 3
    btnManualOrderBuy.addEventListener('click', () => {
        if (currentManualProduct) {
            manualOrderModal.classList.add('hidden');
            openCheckoutFlow(currentManualProduct);
        }
    });
}

function showWizardStep(stepEl) {
    manualStepChooser.classList.add('hidden');
    manualStepCatalog.classList.add('hidden');
    manualStepSpecs.classList.add('hidden');
    
    stepEl.classList.remove('hidden');
}

function renderWizardCatalog(category) {
    // Filter products matching category
    const filtered = productsCache.filter(p => p.category.toLowerCase() === category.toLowerCase());
    
    manualCatalogGrid.innerHTML = '';
    
    if (filtered.length === 0) {
        manualCatalogGrid.innerHTML = `
            <div style="grid-column: 1/-1; text-align:center; padding: 40px 10px; color:var(--text-secondary);">
                <i class="fa-solid fa-face-frown" style="font-size:2.5rem; margin-bottom:12px; color:var(--text-muted);"></i>
                <p>We currently do not have any ${category} in stock in our database.</p>
            </div>
        `;
        return;
    }

    // Group items by Model/Name to display single card per shoe model
    const uniqueModels = [];
    const seen = new Set();
    
    filtered.forEach(p => {
        const key = `${p.brand}-${p.name}`;
        if (!seen.has(key)) {
            seen.add(key);
            uniqueModels.push(p);
        }
    });

    uniqueModels.forEach(p => {
        const card = document.createElement('div');
        card.className = 'footwear-cat-card';
        card.style.cssText = "background: rgba(255,255,255,0.03); border:1px solid var(--border-color); border-radius:10px; overflow:hidden; cursor:pointer; text-align:center; transition:all 0.2s; padding-bottom:12px; display:flex; flex-direction:column; justify-content:space-between;";
        
        card.innerHTML = `
            <div>
                <img src="${getCategoryImgUrl(p.category)}" alt="${p.name}" style="width:100%; height:120px; object-fit:contain; border-bottom:1px solid var(--border-color); background:rgba(255,255,255,0.01); padding:5px;">
                <div style="padding:10px 10px 5px 10px; font-weight:700; font-size:0.85rem; color:var(--text-primary); text-align:left;">${p.brand} ${p.name}</div>
                <div style="font-size:0.75rem; color:var(--color-primary); font-weight:700; text-align:left; padding:0 10px 10px 10px;">From ₹${p.price.toFixed(2)}</div>
            </div>
            <div style="padding: 0 10px;">
                <button class="btn btn-primary btn-select-model btn-block" style="font-size:0.7rem; padding:8px; border-radius:6px;">Select specs</button>
            </div>
        `;

        card.querySelector('.btn-select-model').addEventListener('click', (e) => {
            e.stopPropagation();
            openSpecsWizard(p);
        });

        manualCatalogGrid.appendChild(card);
    });
}

function openSpecsWizard(product) {
    selectedWizardModel = product.name;
    selectedWizardBrand = product.brand;
    
    manualSpecBrand.value = product.brand;
    manualSpecItemName.textContent = product.name;
    manualSpecItemImg.src = getCategoryImgUrl(product.category);
    
    // Populate Color choices for this brand/model
    const colors = [...new Set(productsCache.filter(p => 
        p.category === selectedWizardCategory && 
        p.brand === product.brand && 
        p.name === product.name
    ).map(p => p.color))];

    populateWizardSelect(manualSelectColor, colors, '-- Choose Color --');
    resetWizardSelect(manualSelectSize);
    hideWizardAvailability();
    
    showWizardStep(manualStepSpecs);
}

function populateWizardSelect(selectEl, items, placeholder) {
    selectEl.innerHTML = `<option value="">${placeholder}</option>`;
    items.forEach(item => {
        selectEl.innerHTML += `<option value="${item}">${item}</option>`;
    });
    selectEl.disabled = false;
}

function resetWizardSelect(selectEl) {
    selectEl.innerHTML = `<option value="">-- Choose Option --</option>`;
    selectEl.disabled = true;
}

function hideWizardAvailability() {
    manualAvailabilityCard.classList.add('hidden');
    btnManualOrderBuy.disabled = true;
    btnManualOrderBuy.innerHTML = '<i class="fa-solid fa-bag-shopping"></i> Order Now (Configure Specs)';
    currentManualProduct = null;
}

function checkWizardProductAvailability() {
    const color = manualSelectColor.value;
    const size = parseInt(manualSelectSize.value);

    if (!size) {
        hideWizardAvailability();
        return;
    }

    const match = productsCache.find(p => 
        p.category === selectedWizardCategory && 
        p.brand === selectedWizardBrand && 
        p.name === selectedWizardModel && 
        p.color === color && 
        p.size === size
    );
    
    if (!match) {
        manualAvailabilityCard.classList.remove('hidden');
        manualAvailabilityBadge.textContent = 'UNAVAILABLE';
        manualAvailabilityBadge.className = 'badge badge-danger';
        manualItemPrice.textContent = '₹0.00';
        
        btnManualOrderBuy.disabled = true;
        btnManualOrderBuy.innerHTML = '<i class="fa-solid fa-triangle-exclamation"></i> Item Unavailable';
        currentManualProduct = null;
        return;
    }

    currentManualProduct = match;
    manualAvailabilityCard.classList.remove('hidden');
    manualItemPrice.textContent = `₹${match.price.toFixed(2)}`;

    if (match.stockCount > 0) {
        manualAvailabilityBadge.textContent = `AVAILABLE (${match.stockCount} LEFT)`;
        manualAvailabilityBadge.className = 'badge badge-success';
        
        btnManualOrderBuy.disabled = false;
        btnManualOrderBuy.innerHTML = '<i class="fa-solid fa-cart-shopping"></i> Proceed to Checkout';
    } else {
        manualAvailabilityBadge.textContent = 'OUT OF STOCK';
        manualAvailabilityBadge.className = 'badge badge-danger';
        
        btnManualOrderBuy.disabled = true;
        btnManualOrderBuy.innerHTML = '<i class="fa-solid fa-circle-xmark"></i> Out of Stock';
    }
}

// ================= AI AGENT PROCESS LOOP =================
async function handleChatSubmit(e) {
    e.preventDefault();
    const query = chatInput.value.trim();
    if (!query) return;

    chatInput.value = '';

    appendMessage(query, 'customer');

    const loaderId = appendMessageLoader();

    try {
        const response = await fetch(`${API_BASE_URL}/api/agent/query`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({ query })
        });

        removeMessageLoader(loaderId);

        if (response.ok) {
            const data = await response.json();
            
            const msgDiv = appendMessage(data.reply, 'ai');

            // Save conversation exchange to UI Sidebar History list
            addChatToHistoryList(query, data.reply);

            // Handle inline shoppable product cards dynamically inside chat
            if (data.detectedParams && data.detectedParams.brand && data.detectedParams.color && data.detectedParams.size) {
                const match = productsCache.find(p => 
                    p.brand.toLowerCase() === data.detectedParams.brand.toLowerCase() && 
                    p.color.toLowerCase() === data.detectedParams.color.toLowerCase() && 
                    p.size == data.detectedParams.size
                );
                
                if (match) {
                    appendProductShowcaseCardToMessage(msgDiv, match);
                } else {
                    // Try to list relatable alternate shoes of same brand
                    const relatable = productsCache.filter(p => 
                        p.brand.toLowerCase() === data.detectedParams.brand.toLowerCase() && 
                        p.stockCount > 0
                    ).slice(0, 2);
                    if (relatable.length > 0) {
                        appendRelatableShowcaseCards(msgDiv, relatable);
                    }
                }
            } else if (data.detectedParams && data.detectedParams.brand) {
                const relatable = productsCache.filter(p => 
                    p.brand.toLowerCase() === data.detectedParams.brand.toLowerCase() && 
                    p.stockCount > 0
                ).slice(0, 2);
                if (relatable.length > 0) {
                    appendRelatableShowcaseCards(msgDiv, relatable);
                }
            }

            fetchCatalogProducts();
        } else {
            const errData = await response.json();
            appendMessage(`Error: ${errData.message || 'Failed to process request.'}`, 'ai');
        }
    } catch (error) {
        removeMessageLoader(loaderId);
        appendMessage('Network failure. Cannot reach AI Agent controller.', 'ai');
    }
}

function appendMessage(text, sender) {
    const msgDiv = document.createElement('div');
    msgDiv.className = `msg msg-${sender}`;
    
    const avatarDiv = document.createElement('div');
    avatarDiv.className = 'avatar';
    avatarDiv.innerHTML = sender === 'ai' ? '<i class="fa-solid fa-robot"></i>' : '<i class="fa-solid fa-user"></i>';

    const contentDiv = document.createElement('div');
    contentDiv.className = 'msg-content';
    
    const p = document.createElement('p');
    p.textContent = text;
    contentDiv.appendChild(p);

    msgDiv.appendChild(avatarDiv);
    msgDiv.appendChild(contentDiv);
    
    chatMessages.appendChild(msgDiv);
    chatMessages.scrollTop = chatMessages.scrollHeight;
    return contentDiv;
}

function appendProductShowcaseCardToMessage(contentDiv, product) {
    const card = document.createElement('div');
    card.style.cssText = "background:#ffffff; border:1px solid var(--border-color); border-radius:8px; padding:12px; margin-top:12px; box-shadow:0 3px 8px rgba(168,85,247,0.06); display:flex; justify-content:space-between; align-items:center; gap:15px;";
    
    card.innerHTML = `
        <div>
            <h5 style="margin:0 0 2px 0; font-weight:700; color:var(--text-primary); font-size:0.85rem;">${product.brand} ${product.name}</h5>
            <span style="font-size:0.7rem; color:var(--text-secondary);">Size: ${product.size} | Color: ${product.color} | Rating: ★${product.rating.toFixed(1)}</span>
        </div>
        <div style="text-align:right; flex-shrink:0;">
            <div style="font-family:var(--font-heading); font-weight:800; font-size:0.95rem; color:var(--color-primary); margin-bottom:5px;">₹${product.price.toFixed(2)}</div>
            <button class="btn btn-primary btn-chat-buy" style="font-size:0.7rem; padding:6px 12px; border-radius:15px;">
                <i class="fa-solid fa-cart-shopping"></i> Buy Now
            </button>
        </div>
    `;

    card.querySelector('.btn-chat-buy').addEventListener('click', () => {
        openCheckoutFlow(product);
    });

    contentDiv.appendChild(card);
}

function appendRelatableShowcaseCards(contentDiv, products) {
    const container = document.createElement('div');
    container.style.cssText = "margin-top:12px; display:flex; flex-direction:column; gap:8px;";
    
    const label = document.createElement('div');
    label.style.cssText = "font-size:0.75rem; font-weight:700; color:var(--text-secondary); margin-bottom:2px;";
    label.innerHTML = '<i class="fa-solid fa-list-check"></i> Relatable items available:';
    container.appendChild(label);

    products.forEach(p => {
        const card = document.createElement('div');
        card.style.cssText = "background:#ffffff; border:1px solid var(--border-color); border-radius:8px; padding:10px 12px; box-shadow:0 2px 6px rgba(0,0,0,0.03); display:flex; justify-content:space-between; align-items:center; gap:15px;";
        
        card.innerHTML = `
            <div>
                <h5 style="margin:0 0 1px 0; font-weight:600; color:var(--text-primary); font-size:0.8rem;">${p.brand} ${p.name}</h5>
                <span style="font-size:0.65rem; color:var(--text-secondary);">Size ${p.size} | ${p.color} | Rating ★${p.rating.toFixed(1)}</span>
            </div>
            <div style="text-align:right; flex-shrink:0; display:flex; align-items:center; gap:10px;">
                <span style="font-family:var(--font-heading); font-weight:700; font-size:0.85rem; color:var(--color-primary);">₹${p.price.toFixed(2)}</span>
                <button class="btn btn-primary btn-chat-buy" style="font-size:0.65rem; padding:4px 8px; border-radius:12px;">
                    Buy
                </button>
            </div>
        `;

        card.querySelector('.btn-chat-buy').addEventListener('click', () => {
            openCheckoutFlow(p);
        });

        container.appendChild(card);
    });

    contentDiv.appendChild(container);
}

function appendMessageLoader() {
    const id = 'loader-' + Date.now();
    const loaderDiv = document.createElement('div');
    loaderDiv.id = id;
    loaderDiv.className = 'msg msg-ai';
    
    const avatarDiv = document.createElement('div');
    avatarDiv.className = 'avatar';
    avatarDiv.innerHTML = '<i class="fa-solid fa-robot"></i>';

    const contentDiv = document.createElement('div');
    contentDiv.className = 'msg-content';
    contentDiv.innerHTML = '<span class="cursor"><i class="fa-solid fa-ellipsis-h fa-pulse"></i> Thinking...</span>';

    loaderDiv.appendChild(avatarDiv);
    loaderDiv.appendChild(contentDiv);
    
    chatMessages.appendChild(loaderDiv);
    chatMessages.scrollTop = chatMessages.scrollHeight;
    return id;
}

function removeMessageLoader(id) {
    const el = document.getElementById(id);
    if (el) el.remove();
}

// ================= DATA CATALOG INVENTORY =================
async function fetchCatalogFilters() {
    try {
        const response = await fetch(`${API_BASE_URL}/api/agent/filters`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        if (response.ok) {
            const data = await response.json();
            
            filterBrand.innerHTML = '<option value="">All Brands</option>';
            data.brands.forEach(b => {
                filterBrand.innerHTML += `<option value="${b}">${b}</option>`;
            });

            filterColor.innerHTML = '<option value="">All Colors</option>';
            data.colors.forEach(c => {
                filterColor.innerHTML += `<option value="${c}">${c}</option>`;
            });

            filterSize.innerHTML = '<option value="">All Sizes</option>';
            data.sizes.forEach(s => {
                filterSize.innerHTML += `<option value="${s}">${s}</option>`;
            });
        }
    } catch (e) {
        console.error('Failed loading catalog filters dropdowns', e);
    }
}

async function fetchCatalogProducts() {
    try {
        const response = await fetch(`${API_BASE_URL}/api/agent/products`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        if (response.ok) {
            productsCache = await response.json();
            renderCatalogProducts();
        }
    } catch (e) {
        console.error('Failed loading catalog products', e);
    }
}

function renderCatalogProducts() {
    const targetBrand = filterBrand.value;
    const targetColor = filterColor.value;
    const targetSize = filterSize.value;

    let filtered = productsCache;

    if (targetBrand) {
        filtered = filtered.filter(p => p.brand === targetBrand);
    }
    if (targetColor) {
        filtered = filtered.filter(p => p.color === targetColor);
    }
    if (targetSize) {
        filtered = filtered.filter(p => p.size == targetSize);
    }

    catalogList.innerHTML = '';
    
    if (filtered.length === 0) {
        catalogList.innerHTML = '<p class="text-secondary" style="text-align:center; padding: 20px 0;">No matching shoes in stock.</p>';
        return;
    }

    filtered.forEach(p => {
        const isOutOfStock = p.stockCount === 0;
        const card = document.createElement('div');
        card.className = 'shoe-card';
        card.innerHTML = `
            <div class="shoe-card-header">
                <div>
                    <h4 class="shoe-card-title">${p.name}</h4>
                    <span class="shoe-card-brand">${p.brand}</span>
                </div>
                <span class="shoe-card-price">₹${p.price.toFixed(2)}</span>
            </div>
            <div class="shoe-card-specs">
                <span>Color: <strong>${p.color}</strong></span>
                <span>Size: <strong>${p.size}</strong></span>
            </div>
            <p class="shoe-card-desc">${p.description || 'Premium design with ergonomic fit.'}</p>
            <div class="shoe-card-footer" style="flex-direction:column; gap:10px;">
                <div style="display:flex; justify-content:space-between; width:100%; align-items:center;">
                    <div class="shoe-rating">
                        <i class="fa-solid fa-star"></i> ${p.rating.toFixed(1)} <span>(${p.reviewCount})</span>
                    </div>
                    <span class="shoe-stock ${isOutOfStock ? 'out-of-stock' : 'in-stock'}">
                        ${isOutOfStock ? 'OUT OF STOCK' : p.stockCount + ' LEFT'}
                    </span>
                </div>
                <div style="display:flex; gap:10px; width:100%;">
                    <button class="btn btn-secondary shoe-card-action" style="flex:1;">
                        <i class="fa-solid fa-comments"></i> Ask
                    </button>
                    <button class="btn btn-primary shoe-card-buy" style="flex:1;" ${isOutOfStock ? 'disabled' : ''}>
                        <i class="fa-solid fa-cart-shopping"></i> Buy Now
                    </button>
                </div>
            </div>
        `;

        card.querySelector('.shoe-card-action').addEventListener('click', () => {
            closeCatalog();
            chatInput.value = `I want ${p.brand} shoes of ${p.color.toLowerCase()} color of size ${p.size} no.`;
            chatForm.dispatchEvent(new Event('submit'));
        });

        card.querySelector('.shoe-card-buy').addEventListener('click', () => {
            closeCatalog();
            openCheckoutFlow(p);
        });

        catalogList.appendChild(card);
    });
}

// ================= ADMIN MANAGEMENT PORTAL =================
async function renderAdminInventoryTable() {
    try {
        const response = await fetch(`${API_BASE_URL}/api/agent/products`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        if (response.ok) {
            const products = await response.json();
            adminInventoryList.innerHTML = '';
            
            if (products.length === 0) {
                adminInventoryList.innerHTML = '<tr><td colspan="5" style="text-align:center;">No items in database.</td></tr>';
                return;
            }

            products.forEach(p => {
                const tr = document.createElement('tr');
                tr.innerHTML = `
                    <td><strong>${p.productId}</strong></td>
                    <td>
                        <div>${p.name}</div>
                        <span class="badge badge-indigo" style="font-size:0.6rem; padding: 1px 5px;">${p.brand}</span>
                    </td>
                    <td>Size ${p.size} | ${p.color} | Stock ${p.stockCount}</td>
                    <td>₹${p.price.toFixed(2)}</td>
                    <td>
                        <button class="btn btn-danger btn-icon-delete" data-id="${p.productId}">
                            <i class="fa-solid fa-trash-can"></i>
                        </button>
                    </td>
                `;

                tr.querySelector('.btn-danger').addEventListener('click', () => deleteProduct(p.productId));
                adminInventoryList.appendChild(tr);
            });
        }
    } catch (e) {
        showToast('Error loading admin inventory table.', 'error');
    }
}

adminAddProductForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const productId = document.getElementById('prod-id').value.trim();
    const brand = document.getElementById('prod-brand').value;
    const name = document.getElementById('prod-name').value.trim();
    const color = document.getElementById('prod-color').value;
    const size = parseInt(document.getElementById('prod-size').value);
    const price = parseFloat(document.getElementById('prod-price').value);
    const stockCount = parseInt(document.getElementById('prod-stock').value);
    const ratingInput = document.getElementById('prod-rating').value;
    const rating = ratingInput ? parseFloat(ratingInput) : null;
    const description = document.getElementById('prod-desc').value.trim();

    const productPayload = {
        productId, brand, name, color, size, price, stockCount, rating, description
    };

    try {
        const response = await fetch(`${API_BASE_URL}/api/admin/products`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(productPayload)
        });

        const data = await response.json();

        if (response.ok) {
            showToast(data.message || 'Product added successfully!', 'success');
            adminAddProductForm.reset();
            renderAdminInventoryTable();
            fetchCatalogFilters();
            fetchCatalogProducts();
        } else {
            showToast(data.message || 'Failed adding product.', 'error');
        }
    } catch (error) {
        showToast('Connection error adding product.', 'error');
    }
});

async function deleteProduct(productId) {
    try {
        const response = await fetch(`${API_BASE_URL}/api/admin/products/${productId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        const data = await response.json();

        if (response.ok) {
            showToast(data.message || 'Product removed successfully.', 'success');
            renderAdminInventoryTable();
            fetchCatalogFilters();
            fetchCatalogProducts();
        } else {
            showToast(data.message || 'Deletion failed.', 'error');
        }
    } catch (e) {
        showToast('Connection error deleting product.', 'error');
    }
}

// ================= DANGER ZONE: ACCOUNT DELETION =================
async function deleteUserAccount() {
    confirmModal.classList.add('hidden');

    try {
        const response = await fetch(`${API_BASE_URL}/api/auth/delete`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        const data = await response.json();

        if (response.ok) {
            showToast('Your account was permanently deleted.', 'success');
            localStorage.removeItem('jwt_token');
            localStorage.removeItem('chat_history_' + currentUser.username);
            localStorage.removeItem('current_user');
            token = null;
            currentUser = null;
            showHomepage();
        } else {
            showToast(data.message || 'Failed to delete account.', 'error');
        }
    } catch (error) {
        showToast('Error deleting account.', 'error');
    }
}
